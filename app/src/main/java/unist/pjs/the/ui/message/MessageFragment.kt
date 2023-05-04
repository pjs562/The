package unist.pjs.the.ui.message

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.data.ChatRoom
import unist.pjs.the.data.Room
import unist.pjs.the.databinding.FragmentMessageBinding
import unist.pjs.the.extends.*
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null

    private val viewModel: MessageViewModel by viewModels()

    private lateinit var callback: OnBackPressedCallback

    private var chatRoomList: ArrayList<ChatRoom> = ArrayList()

    private var isFirst: Boolean = true

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)

        val db = Firebase.firestore
        db.collection(Preferences.userId).whereGreaterThan("unread", -1)
            .addSnapshotListener { value, e ->
                if (e !== null) {
                    Log.e("TEST", "ListenFailed.", e)
                    return@addSnapshotListener
                }
                isFirst = false
                chatRoomList = ArrayList()

                for (doc in value!!) {
                    val roomId = doc.id
                    val userInfo = doc.data
                    val userName = userInfo["name"] as String
                    val content = userInfo["content"] as String
                    val unread = userInfo["unread"] as Long
                    val timestamp = userInfo["time"] as Timestamp
                    val time =
                        (timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000).timeDiff2()
                    chatRoomList.add(ChatRoom(roomId, userName, content, unread, time))
                }

                viewModel.upDateRoomList(chatRoomList)
            }

        binding.cvList.setContent {
            TheTheme {
                ChatRoomList(viewModel = viewModel) { dest -> findNavController().navigate(dest) }
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etSearch.text.isNotEmpty()) {
                    viewModel.upDateRoomList(chatRoomList.filter { it.name.contains(p0.toString()) })
                } else {
                    viewModel.upDateRoomList(chatRoomList)
                }
            }
        })

        initLiveDataObservers()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLiveDataObservers() {
        viewModel.chatRoomList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                binding.tvNoList.visibility = View.GONE
            else
                binding.tvNoList.visibility = View.VISIBLE
        }

        viewModel.roomList.observe(viewLifecycleOwner){ list ->
            list?.let { roomList ->
                for (room in roomList){
                    val r = chatRoomList.find { it.id == room.roomid }
                    if(r == null){
                        if(room.couple[0] == Preferences.userName){
                            chatRoomList.add(ChatRoom(id = room.roomid, name = room.couple[1], content = "", unread = 0L, ""))
                        }
                    }
                }
                viewModel.upDateRoomList2(chatRoomList)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}