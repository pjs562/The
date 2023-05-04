package unist.pjs.the.ui.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentCreatePostBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast
import unist.pjs.the.ui.login.GridCellList
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null

    private val viewModel: CreatePostViewModel by viewModels()

    private val binding get() = _binding!!

    private var type = "notice"
    private var _id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        viewModel.getBulletin(_id)
        viewModel.changeType(type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        binding.tvOk.setOnClickListener {
            if(binding.etTitle.text.isNotEmpty() && binding.etContent.text.isNotEmpty()){
                if(_id.isNotEmpty())
                    viewModel.putBoard(_id, binding.etTitle.text.toString(), binding.etContent.text.toString())
                else
                    viewModel.postBoard(binding.etTitle.text.toString(), binding.etContent.text.toString())
            }else{
                showToast("제목과 내용을 입력해주세요.")
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

    private fun initArgs(){
        arguments?.apply {
            type = getString("type", "")
            _id = getString("_id", "")
        }
    }

    private fun initLiveDataObservers() {
        viewModel.bulletin.observe(viewLifecycleOwner) {
            if(it.name == Preferences.userName){
                if(_id.isNotEmpty()){
                    viewModel.upDateBulletin(it)
                    showToast("게시글이 수정되었습니다.")
                }
                else
                    showToast("게시글이 작성되었습니다.")
                findNavController().popBackStack()
            }
        }

        viewModel.bulletin2.observe(viewLifecycleOwner){
            binding.etTitle.setText(it.title)
            binding.etContent.setText(it.content)
        }
    }
}