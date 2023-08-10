package unist.pjs.the.ui.login

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentSignupBinding
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null

    private val viewModel: LoginViewModel by viewModels()

    private val binding get() = _binding!!

    private var gender = ""
    private var name = ""
    private var birthday = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.btnNext.setOnClickListener {
            if(validationCheck()){
                val action = SignupFragmentDirections.actionSignupFragmentToSignupFragment3(gender = gender, name = name, birthday = birthday)
                findNavController().navigate(action)
            }
        }

        binding.btnPrev.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCheckDuplication.setOnClickListener {
            viewModel.checkName(binding.etName.text.toString())
        }

        binding.btnMen.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.btnWomen.isSelected = false
            binding.btnWomen.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            if(it.isSelected)
                binding.btnMen.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.btnWomen.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.btnMen.isSelected = false
            binding.btnMen.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            if(it.isSelected)
                binding.btnWomen.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.tvYear.setOnClickListener {
            datePickerDialog()
        }

        binding.tvMonth.setOnClickListener {
            datePickerDialog()
        }

        binding.tvDay.setOnClickListener {
            datePickerDialog()
        }

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.tvAvailableName.text = ""
            }
        })
        initLiveDataObservers()

        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.duplicate.observe(viewLifecycleOwner) {
            binding.tvAvailableName.visibility = View.VISIBLE
            binding.tvAvailableName.text = if (it) "중복된 이름/닉네임 입니다." else "사용 가능한 이름/닉네임 입니다."
        }
    }

    private fun datePickerDialog(){
        val beforeYear = 2000
        val beforeMonth = 1
        val beforeDay = 1

        @Suppress("DEPRECATION")
        val datePickerDialog = DatePickerDialog(
            requireContext(), AlertDialog.THEME_HOLO_LIGHT,
            { _, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                val monthStr = if (month < 10) {
                    "0$month"
                } else {
                    month.toString()
                }
                val day = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                binding.tvYear.text = year.toString()
                binding.tvMonth.text = monthStr
                binding.tvDay.text = day
                birthday = year.toString() + monthStr + day
            }, beforeYear, beforeMonth, beforeDay
        )
        datePickerDialog.show()
    }
    private fun validationCheck(): Boolean{
        name = binding.etName.text.toString()

        if (name.isEmpty()) {
            showToast("이름을 입력하세요.")
            return false
        }

        if(binding.tvAvailableName.text != "사용 가능한 이름/닉네임 입니다."){
            showToast("중복을 확인해주세요.")
            return false
        }

        if(birthday.length != 8){
            showToast("생년월일을 확인해주세요")
            return false
        }

        gender = when {
            binding.btnMen.isSelected -> {
                "남자"
            }

            binding.btnWomen.isSelected -> {
                "여자"
            }
            else -> {
                showToast("성별을 선택하세요")
                return false
            }
        }
        return true
    }
}