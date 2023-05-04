package unist.pjs.the.ui.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentVoteBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class VoteFragment : Fragment() {
    private var _binding: FragmentVoteBinding? = null

    private val viewModel: VoteViewModel by viewModels()

    private var selected: Boolean = false
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVoteBinding.inflate(inflater, container, false)

        binding.btnLeft.setOnClickListener {
            if (!selected) {
                selected = true
                viewModel.postPoll(Preferences.userId, "left", "poll")
                it.isSelected = !it.isSelected
                binding.btnLeft.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.white))
            }
        }

        binding.btnRight.setOnClickListener {
            if (!selected) {
                selected = true
                viewModel.postPoll(Preferences.userId, "right", "poll")
                it.isSelected = !it.isSelected
                binding.btnRight.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.white))
            }
        }

        initLiveDataObservers()

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.confirm.observe(viewLifecycleOwner) {
            if (it.ok == "success")
                showToast("선택 완료!")
            else
                showToast(it.error)
        }
    }
}