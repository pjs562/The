package unist.pjs.the.ui.matching

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentMatchingBinding
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class MatchingFragment : Fragment() {

    private var _binding: FragmentMatchingBinding? = null

    private val viewModel: MatchingViewModel by viewModels()

    private val binding get() = _binding!!

    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initGetPeoples()
        viewModel.onTypeChanged(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cvCategory.setContent {
            TheTheme {
                MatchingCategory(viewModel)
            }
        }

        binding.cvList.setContent {
            TheTheme {
                MatchingList(viewModel, onNavigate = {dest -> findNavController().navigate(dest)})
            }
        }

        binding.mtb.setOnMenuItemClickListener {
            val action = MatchingFragmentDirections.actionNavigationMatchingToHeartDialog()
            findNavController().navigate(action)
            true
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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