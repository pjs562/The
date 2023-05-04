package unist.pjs.the.ui.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import unist.pjs.the.data.Chat
import unist.pjs.the.data.ChatRoom
import unist.pjs.the.databinding.FragmentMessageDetailBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.convertTimestampToDate
import unist.pjs.the.extends.showToast
import unist.pjs.the.extends.timeDiff2
import unist.pjs.the.ui.board.BalanceDetailFragmentDirections
import unist.pjs.the.ui.theme.TheTheme
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class MessageDetailFragment : Fragment() {

    private var _binding: FragmentMessageDetailBinding? = null

    private val viewModel: MessageDetailViewModel by viewModels()

    private val binding get() = _binding!!

    private lateinit var id: String

    private var isFirst: Boolean = true

    private var db = Firebase.firestore

    private var user1Online: Boolean = false

    private var user2Online: Boolean = false

    private lateinit var status: HashMap<*, *>

    private lateinit var user1: String

    private lateinit var user2: String

    private lateinit var chat: CollectionReference

    private var unread = 0L

    private var isDestroy: Boolean = false

    private lateinit var chatList: ArrayList<Chat>

    private lateinit var content: String

    private lateinit var time: Timestamp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        viewModel.getProfileList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMessageDetailBinding.inflate(inflater, container, false)

        db.collection("room").document(id).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            Log.e("TEST", "111")
            if (snapshot != null && snapshot.exists()) {
                snapshot.data?.let {
                    Log.e("TEST", "snapshot.data: ${snapshot.data}")
                    status = it["status"] as HashMap<*, *>
                    user1 = status["user1"] as String
                    user2 = status["user2"] as String

                    user1Online = status["user1Online"] as Boolean
                    user2Online = status["user2Online"] as Boolean

                    isFirst = if (Preferences.userId == user1) {
                        viewModel.setProfile(user2)
                        true
                    } else {
                        viewModel.setProfile(user1)
                        false
                    }

                    if (!isDestroy) {
                        if (isFirst) {
                            db.collection("room").document(id).update(mapOf(
                                "status.user1Online" to true
                            ))
                        } else {
                            db.collection("room").document(id).update(mapOf(
                                "status.user2Online" to true
                            ))
                        }
                    }
                }
            }
        }

        db.collection(Preferences.userId).document(id).update(mapOf(
            "unread" to 0
        ))

        db.collection("room").document(id).collection("message")
            .orderBy("time", Query.Direction.DESCENDING).addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("TEST", "Listen failed.", e)
                    return@addSnapshotListener
                }

                chatList = ArrayList()

                for (doc in value!!) {
                    val roomId = doc.id
                    val data = doc.data
                    val person = data["person"] as String
                    val timestamp = data["time"] as Timestamp
                    val time =
                        (timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000).convertTimestampToDate()
                    val content = data["content"] as String
                    chatList.add(Chat(person, time, content))
                }
                viewModel.upDateChatList(chatList.reversed())
            }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.ivReport.setOnClickListener {
            viewModel.user.value?.let { it1 ->
                val action =  MessageDetailFragmentDirections.actionMessageDetailFragmentToReportDialog(it1, "person")
                findNavController().navigate(action)
            }
        }


        binding.ivSend.setOnClickListener {
            if (binding.etSend.text.isNotEmpty()) {
                if (isFirst) {
                    db.collection(user1).document(id).update(mapOf(
                        "content" to binding.etSend.text.toString(),
                        "time" to Timestamp(Date())
                    ))

                    if (!user2Online) {
                        content = binding.etSend.text.toString()
                        time = Timestamp(Date())

                        db.collection(user2).document(id).update(mapOf(
                            "content" to binding.etSend.text.toString(),
                            "unread" to ++unread,
                            "time" to Timestamp(Date())
                        ))
                    } else {
                        unread = 0L
                    }
                } else {
                    db.collection(user2).document(id).update(mapOf(
                        "content" to binding.etSend.text.toString(),
                        "time" to Timestamp(Date())
                    ))

                    if (!user1Online) {
                        content = binding.etSend.text.toString()
                        time = Timestamp(Date())
                        db.collection(user1).document(id).update(mapOf(
                            "content" to binding.etSend.text.toString(),
                            "unread" to ++unread,
                            "time" to Timestamp(Date())
                        ))
                    } else {
                        unread = 0L
                    }
                }

                chat = db.collection("room").document(id).collection("message")
                chat.document().set(
                    hashMapOf(
                        "content" to binding.etSend.text.toString(),
                        "person" to Preferences.userName,
                        "time" to Timestamp(Date())
                    )
                )
                binding.etSend.setText("")
            } else {
                showToast("댓글을 입력해주세요.")
            }
        }

        binding.cvList.setContent {
            TheTheme {
                ChatList(viewModel = viewModel)
            }
        }

        initLiveDataObservers()

        return binding.root
    }

    private fun initArgs() {
        arguments?.apply {
            id = getString("id", "")
            user1 = Preferences.userId
            user2 = id
        }
    }

    override fun onDetach() {
        super.onDetach()
        isDestroy = true
        if (isFirst) {
            db.collection("room").document(id).update(mapOf(
                "status.user1Online" to false
            ))
        } else {
            db.collection("room").document(id).update(mapOf(
                "status.user2Online" to false
            ))
        }
    }

    private fun initLiveDataObservers() {
        viewModel.user.observe(viewLifecycleOwner) {
            Log.e("TEST", "user: $it")
            db.collection(it).document(id)
                .get()
                .addOnSuccessListener { document ->

                    if(document != null){
                        document.data?.get("unread")?.let { count ->
                            unread = count as Long
                        }
                        Log.e("TEST", "document.data: ${document.data}")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("TEST", "get failed with", exception)
                }
        }
    }
}