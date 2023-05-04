package unist.pjs.the.ui.matching

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentProfileDetailBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast
import unist.pjs.the.ui.login.GridCellList
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class ProfileDetailFragment : Fragment() {

    private lateinit var binding: FragmentProfileDetailBinding
    private val viewModel: ProfileDetailViewModel by viewModels()
    private var name = ""
    private var age = ""
    private var cellGroup = ""
    private var imageUrl = ""
    private var gender = ""
    private var bio = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        viewModel.getMatch(name)

        binding.tvName.text = "$name       $age"
        binding.tvCellName.text = cellGroup
        binding.tvContent.text = bio.ifEmpty { "안녕하세요! 반갑습니다!" }

        if (Preferences.userName == name) {
            binding.ibHeart.visibility = View.INVISIBLE
            binding.ibMessage.visibility = View.INVISIBLE
        }

        setFragmentResultListener("like") { _, bundle ->
            when (bundle.getInt("isHeart", 0)) {
                1 -> viewModel.getMatch(name)
                2 -> viewModel.getMatch(name)
            }
        }

        binding.ivProfileImage.setContent {
            TheTheme {
                ProfileImage(imageUrl)
            }
        }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            setOnMenuItemClickListener {
                val action =
                    ProfileDetailFragmentDirections.actionNavigationProfileDetailToHeartDialog()
                findNavController().navigate(action)
                true
            }
        }

        binding.ibHeart.setOnClickListener {
            val action =
                ProfileDetailFragmentDirections.actionNavigationProfileDetailToLikeDialog(name,
                    gender)
            findNavController().navigate(action)
        }

        binding.ibMessage.setOnClickListener {
            viewModel.getRoomId(name)
        }

        initLiveDataObservers()
        return binding.root
    }

    private fun initArgs() {
        arguments?.apply {
            name = getString("name", "")
            age = getString("age", "")
            imageUrl = getString("imageUrl", "")
            cellGroup = getString("cellGroup", "")
            gender = getString("gender", "men")
            bio = getString("bio", "")
        }
    }

    @Composable
    fun ProfileImage(imageUrl: String) {
        Surface(shape = AbsoluteRoundedCornerShape(40.dp), modifier = Modifier.fillMaxSize()) {
            Image(modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(model = "https://api.theloveyouth.com/$imageUrl",
                    error = if (gender == "남자") painterResource(id = R.drawable.ic_men)
                    else painterResource(id = R.drawable.ic_girl)),
                contentDescription = null)
        }
    }

    private fun initLiveDataObservers() {
        viewModel.isHeart.observe(viewLifecycleOwner) {
            binding.ibHeart.isClickable = false
            if (it)
                binding.ibHeart.setBackgroundResource(R.drawable.ic_ren_heart)
            else
                binding.ibHeart.setBackgroundResource(R.drawable.ic_green_like)
        }

        viewModel.roomId.observe(viewLifecycleOwner) {
            if(it.equals("no")){
                showToast("상대방과 매칭되지 않았습니다.")
            }else if(it.isEmpty()){
            } else{
                val action = ProfileDetailFragmentDirections.actionNavigationProfileDetailToMessageDetailFragment(it)
                findNavController().navigate(action)
                viewModel.resetRoomId()
            }
        }
    }
}