package unist.pjs.the.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentProfileContentBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class ProfileContentFragment : Fragment() {

    private lateinit var binding: FragmentProfileContentBinding

    private val viewModel: ProfileContentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileContentBinding.inflate(inflater, container, false)

        binding.tvAge.text = Preferences.userAge
        binding.tvName.text = Preferences.userName
        binding.tvCellName.text = Preferences.userGroup
        binding.etContent.setText(Preferences.userBio)
        binding.tvInputLength.text = Preferences.userBio.length.toString() + "/500"
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.tvInputLength.text = s.length.toString() + "/500"
            }
        })

        binding.tvDone.setOnClickListener {
            viewModel.postUpdateContent(Preferences.userName, binding.etContent.text.toString())
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
        viewModel.userInfo.observe(viewLifecycleOwner) {
            if(it.bio.isNullOrEmpty()){
                showToast("자기소개 내용이 없습니다.")
            }else{
                Preferences.userBio = it.bio
                findNavController().popBackStack()
            }
        }
    }

}