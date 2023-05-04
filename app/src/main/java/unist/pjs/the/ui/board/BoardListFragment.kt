package unist.pjs.the.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentBoardListBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class BoardListFragment : Fragment(){
    private var _binding: FragmentBoardListBinding? = null

    private val viewModel: BoardListViewModel by viewModels()

    private val binding get() = _binding!!

    private var type = "notice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBoardListBinding.inflate(inflater, container, false)
        viewModel.changeType(type)
        when (type) {
            "notice" -> {
                binding.tvType.text = "공지사항"
                if(Preferences.userAdmin == "false"){
                    binding.ivPlus.visibility = View.INVISIBLE
                }
            }

            "free" -> {
                binding.tvType.text = "자유게시판"
            }
            "pray" -> {
                binding.tvType.text = "기도요청"
            }

            "qt" -> {
                binding.tvType.text = "큐티"
                if(Preferences.userAdmin == "false"){
                    binding.ivPlus.visibility = View.INVISIBLE
                }
            }
        }

        binding.cvList.setContent {
            TheTheme {
                BoardList(type, viewModel) { dest -> findNavController().navigate(dest) }
            }
        }

        binding.ivPlus.setOnClickListener {
            val action = BoardListFragmentDirections.actionBoardListFragmentToCreatePostFragment(type)
            findNavController().navigate(action)
        }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    private fun initArgs(){
        arguments?.apply {
            type = getString("type", "")
        }
    }
}