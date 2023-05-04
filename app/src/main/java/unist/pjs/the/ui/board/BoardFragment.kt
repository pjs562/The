package unist.pjs.the.ui.board

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import unist.pjs.the.databinding.FragmentBoardBinding
import unist.pjs.the.extends.showToast

class BoardFragment : Fragment() {

    private var _binding: FragmentBoardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)

        binding.tvNotice.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToBoardListFragment(type = "notice")
            findNavController().navigate(action)
        }

        binding.tvFree.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToBoardListFragment(type = "free")
            findNavController().navigate(action)
        }

        binding.tvPray.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToBoardListFragment(type = "pray")
            findNavController().navigate(action)
        }

        binding.tvQt.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToBoardListFragment(type = "qt")
            findNavController().navigate(action)
        }

        binding.tvBalance.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToBalanceFragment()
            findNavController().navigate(action)
        }

        binding.tvVote.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToVoteFragment()
            findNavController().navigate(action)
        }

        binding.tvMouse.setOnClickListener {
            val action = BoardFragmentDirections.actionNavigationBoardToReportDialog(type = "바퀴입")
            findNavController().navigate(action)
        }

        return binding.root
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