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
import unist.pjs.the.databinding.FragmentLoginBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val viewModel: LoginViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.btnSignup.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToPrivacyFragment()
            findNavController().navigate(action)
        }

        binding.btnSendCertificationNumber.setOnClickListener {
            if (binding.etPhoneNumber.length() != 11)
                showToast("올바른 전화번호를 입력해주세요.")
            else {
                viewModel.getCheck(binding.etName.text.toString(),
                    binding.etPhoneNumber.text.toString())
            }
        }

        binding.btnCertificationAgain.setOnClickListener {
            if (binding.etPhoneNumber.length() != 11)
                showToast("올바른 전화번호를 입력해주세요.")
            else {
                viewModel.getCheck(binding.etName.text.toString(),
                    binding.etPhoneNumber.text.toString())
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.postConfirm(RequestAuth(binding.etName.text.toString(),
                binding.etCertificationNumber.text.toString()))
        }

        initLiveDataObservers()
        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (it.name.isNullOrEmpty()) {
                showToast("잘못된 정보를 입력했습니다.")
            } else {
                Preferences.userName = it.name
                Preferences.userId = it._id
                viewModel.postToken(Preferences.userId, Preferences.fcmToken)
            }
        }

        viewModel.check.observe(viewLifecycleOwner) {
            if (it.ok == "success") {
                showToast("인증번호 4자리를 보냈습니다. 확인해주세요.")
            } else {
                showToast("없는 이름 또는 전화번호입니다. 회원가입해주세요.")
            }
        }

        viewModel.confirm.observe(viewLifecycleOwner) {
            if(it.ok == "success"){
                val action = LoginFragmentDirections.actionLoginFragmentToNavigationMain(Preferences.userName)
                findNavController().navigate(action)
            }
            else
                showToast("회원가입을 해주세요.")
        }
    }
}