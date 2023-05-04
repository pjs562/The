package unist.pjs.the.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.data.RequestAuth
import unist.pjs.the.databinding.FragmentSignup3Binding
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class SignupFragment3 : Fragment() {

    private var _binding: FragmentSignup3Binding? = null

    private val viewModel: LoginViewModel by viewModels()

    private val binding get() = _binding!!

    private var gender = ""
    private var name = ""
    private var birthday = ""
    private var phoneNumber = ""
    private var confirm = false
    private var auth_no = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignup3Binding.inflate(inflater, container, false)

        binding.btnPrev.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnGetAuth.setOnClickListener {
            if(binding.etPhoneNumber.length() != 11)
                showToast("올바른 전화번호를 입력해주세요")
            else{
                viewModel.getSendAuth(name, binding.etPhoneNumber.text.toString())
            }
        }

        binding.btnCertification.setOnClickListener {
            if(binding.etCertificationNumber.length() != 4)
                showToast("인증번호 4자리를 확인해주세요.")
            else
                viewModel.postConfirm(RequestAuth(name, binding.etCertificationNumber.text.toString()))
        }

        binding.btnCertificationAgain.setOnClickListener {
            if(binding.etPhoneNumber.length() != 11)
                showToast("올바른 전화번호를 입력해주세요.")
            else{
                viewModel.getSendAuth(name, binding.etPhoneNumber.text.toString())
            }
        }

        binding.btnNext.setOnClickListener {
            if(!confirm){
                showToast("전화번호 인증을 받아주세요.")
            }else{
                val action = SignupFragment3Directions.actionSignupFragment3ToSignupFragment2(name, birthday, gender, phoneNumber, auth_no)
                findNavController().navigate(action)
            }
        }

        initLiveDataObservers()

        return binding.root
    }

    private fun initArgs(){
        arguments?.apply {
            name = getString("name", "")
            gender = getString("gender", "")
            birthday = getString("birthday", "")
        }
    }

    private fun initLiveDataObservers() {
        viewModel.response.observe(viewLifecycleOwner){
            if (it == "success"){
                showToast("인증번호 4자리를 보냈습니다. 확인해주세요.")
            } else {
                showToast("인증번호를 다시 받아주세요.")
            }
        }

        viewModel.userInfo.observe(viewLifecycleOwner){
            if (it.ok.isNullOrEmpty()){
                confirm = false
                showToast("인증번호가 올바르지 않습니다. 다시 확인해주세요")
            }else{
                confirm = true
                auth_no = binding.etCertificationNumber.text.toString()
                phoneNumber = binding.etPhoneNumber.text.toString()
                showToast("인증되었습니다.")
            }
        }
    }
}