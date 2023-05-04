package unist.pjs.the.ui.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentBalanceDetailBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.hideKeyBoardExtend
import unist.pjs.the.extends.showToast
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class BalanceDetailFragment : Fragment() {

    private var _binding: FragmentBalanceDetailBinding? = null

    private val viewModel: BalanceDetailViewModel by viewModels()

    private val binding get() = _binding!!

    private var _id = "_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        viewModel.getProfileList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBalanceDetailBinding.inflate(inflater, container, false)
        viewModel.getBalance(_id)
        initLiveDataObservers()

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.ivReport.setOnClickListener {
            val action = BalanceDetailFragmentDirections.actionBalanceDetailFragmentToReportDialog(_id, type = "balance")
            findNavController().navigate(action)
        }

        binding.ivSend.setOnClickListener {
            if(binding.etSend.text.isNotEmpty()){
                viewModel.postReply(Preferences.userName, _id, binding.etSend.text.toString())
                binding.etSend.setText("")
                hideKeyBoardExtend(binding.etSend)
            } else {
                showToast("댓글을 입력해주세요.")
            }
        }

        binding.ivModify.setOnClickListener {
            val action = BalanceDetailFragmentDirections.actionBalanceDetailFragmentToCreateBalanceFragment(_id)
            findNavController().navigate(action)
        }

        binding.ivDelete.setOnClickListener {
            viewModel.deleteBalance(_id)
        }

        return binding.root
    }

    private fun initArgs(){
        arguments?.apply {
            _id = getString("_id", "")
        }
    }

    private fun initLiveDataObservers() {
        viewModel.balance.observe(viewLifecycleOwner){
            binding.cvList.setContent {
                TheTheme {
                    CommentBalanceList(viewModel)
                }
            }

            if(Preferences.userName == it.name){
                binding.ivModify.visibility = View.VISIBLE
                binding.ivDelete.visibility = View.VISIBLE
                binding.ivReport.visibility = View.GONE
            }
        }

        viewModel.confirm.observe(viewLifecycleOwner){
            if(it.ok == "success")
                findNavController().popBackStack()
            else
                showToast(it.error)
        }
    }
}