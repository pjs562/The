package unist.pjs.the.ui.board

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentBalanceBinding
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class BalanceFragment : Fragment() {
    private var _binding: FragmentBalanceBinding? = null
    private val viewModel: BalanceViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBalanceBinding.inflate(inflater, container, false)
        viewModel.count = 20
        viewModel.deleteBalance()
        viewModel.getBalanceList("")

        binding.cvList.setContent {
            TheTheme {
                BoardBalanceList(viewModel) { dest -> findNavController().navigate(dest) }
            }
        }

        binding.ivPlus.setOnClickListener {
            val action = BalanceFragmentDirections.actionBalanceFragmentToCreateBalanceFragment()
            findNavController().navigate(action)
        }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

}