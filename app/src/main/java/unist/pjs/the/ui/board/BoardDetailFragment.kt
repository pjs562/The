package unist.pjs.the.ui.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentBoardDetailBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.hideKeyBoardExtend
import unist.pjs.the.extends.showToast
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class BoardDetailFragment : Fragment() {
    private var _binding: FragmentBoardDetailBinding? = null

    private val viewModel: BoardDetailViewModel by viewModels()

    private val binding get() = _binding!!

    private var type = "notice"
    private var _id = "_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        viewModel.getProfileList()
        viewModel.setType(type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBoardDetailBinding.inflate(inflater, container, false)

        viewModel.getBulletin(_id)

        initLiveDataObservers()

        when (type) {
            "notice" -> {
                binding.tvType.text = "공지사항"
            }
            "free" -> {
                binding.tvType.text = "자유게시판"
            }

            "qt" -> {
                binding.tvType.text = "큐티"
            }

            else -> {
                binding.tvType.text = "기도요청"
            }
        }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.ivModify.setOnClickListener {
            val action = BoardDetailFragmentDirections.actionBoardDetailFragmentToCreatePostFragment(type, _id)
            findNavController().navigate(action)
        }

        binding.ivDelete.setOnClickListener {
            viewModel.bulletin.value?.let { item -> viewModel.deleteBoard(item._id) }
        }

        binding.ivReport.setOnClickListener {
            val action = BoardDetailFragmentDirections.actionBoardDetailFragmentToReportDialog(_id,type = type)
            findNavController().navigate(action)
        }

        binding.ivSend.setOnClickListener {
            if(binding.etSend.text.isNotEmpty()) {
                viewModel.postReply(Preferences.userName, _id, binding.etSend.text.toString())
                binding.etSend.setText("")
                hideKeyBoardExtend(binding.etSend)
            } else {
                showToast("댓글을 입력해주세요.")
            }
        }
        return binding.root
    }

    private fun initArgs(){
        arguments?.apply {
            type = getString("type", "")
            _id = getString("_id", "")
        }
    }

    private fun initLiveDataObservers() {
        viewModel.confirm.observe(viewLifecycleOwner){
            if(it.ok == "success")
                findNavController().popBackStack()
            else
                showToast(it.error)
        }

        viewModel.bulletin.observe(viewLifecycleOwner){
            binding.cvList.setContent {
                TheTheme {
                    CommentList(viewModel)
                }
            }

            if(Preferences.userName == it.name){
                binding.ivModify.visibility = View.VISIBLE
                binding.ivDelete.visibility = View.VISIBLE
                binding.ivReport.visibility = View.GONE
            }
        }
    }
}