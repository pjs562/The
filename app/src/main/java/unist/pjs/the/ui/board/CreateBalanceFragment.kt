package unist.pjs.the.ui.board


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentCreateBalanceBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class CreateBalanceFragment : Fragment() {
    private var _binding: FragmentCreateBalanceBinding? = null

    private val viewModel: CreateBalanceViewModel by viewModels()

    private val binding get() = _binding!!

    private var _id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        viewModel.getBalance(_id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateBalanceBinding.inflate(inflater, container, false)

        binding.tvOk.setOnClickListener {
            if(binding.etBalance1.text.isNotEmpty() && binding.etBalance1.text.isNotEmpty()){
                if(_id.isNotEmpty())
                    viewModel.putBalance(_id, binding.etBalance1.text.toString(), binding.etBalance2.text.toString())
                else
                    viewModel.postBalance(binding.etBalance1.text.toString(), binding.etBalance2.text.toString())
            }else{
                showToast("벨런스 내용을 입력해주세요.")
            }
        }

        binding.mtb.apply {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        initLiveDataObservers()
        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.balance.observe(viewLifecycleOwner) {
            if(it.name == Preferences.userName){
                if(_id.isNotEmpty()){
                    viewModel.upDateBalance(it)
                    showToast("게시글이 수정되었습니다.")
                }
                else
                    showToast("게시글이 작성되었습니다.")
                findNavController().popBackStack()
            }
        }

        viewModel.balance2.observe(viewLifecycleOwner){
            binding.etBalance1.setText(it.left)
            binding.etBalance2.setText(it.right)
        }
    }

    private fun initArgs(){
        arguments?.apply {
            _id = getString("_id", "")
        }
    }

}