package unist.pjs.the.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.DialogReportBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class ReportDialog: Fragment() {
    private var _binding: DialogReportBinding? = null

    private val viewModel: ReportViewModel by viewModels()

    private val binding get() = _binding!!

    var _id = ""
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogReportBinding.inflate(inflater, container, false)

        if(type == "바퀴입"){
            binding.tvType.text = "바퀴더달린입"
        }

        binding.tvOk.setOnClickListener {
            if (binding.etContent.text.isNotEmpty()){
                if(type == "바퀴입"){
                    viewModel.postReport("익명", "1", binding.etContent.text.toString(), type)
                }else{
                    viewModel.postReport(Preferences.userName, _id, binding.etContent.text.toString(), type)
                }
            } else {
                showToast("내용을 입력해주세요.")
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
        viewModel.confirm.observe(viewLifecycleOwner) {
            if(it.ok == "success")
                findNavController().popBackStack()
            else
                showToast(it.error)
        }
    }
}