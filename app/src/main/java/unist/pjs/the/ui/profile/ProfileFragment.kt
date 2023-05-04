package unist.pjs.the.ui.board

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.compose.rememberAsyncImagePainter
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentProfileBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast
import unist.pjs.the.ui.matching.ProfileDetailFragmentDirections
import unist.pjs.the.ui.matching.ProfileImage
import unist.pjs.the.ui.theme.TheTheme

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.tvCellName.text = "${Preferences.userGroup} 목장"
        binding.tvName.text = "${Preferences.userName} ${Preferences.userAge}"

        binding.ivProfileImage.setContent {
            TheTheme {
                ProfileImage(Preferences.userImage)
            }
        }

        binding.tvModify.setOnClickListener {
            val action = ProfileFragmentDirections.actionNavigationProfileToProfileModifyFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    fun ProfileImage(imageUrl: String) {
        Surface(shape = AbsoluteRoundedCornerShape(40.dp), modifier = Modifier.fillMaxSize()) {
            Image(modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(model = "https://api.theloveyouth.com/$imageUrl",
                    error = if (Preferences.userGender == "남자") painterResource(id = R.drawable.ic_men)
                    else painterResource(id = R.drawable.ic_girl)),
                contentDescription = null)
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