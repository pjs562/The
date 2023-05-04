package unist.pjs.the.ui.matching

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.DialogLikeBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class LikeDialog: DialogFragment() {

    private var _binding: DialogLikeBinding? = null

    private val viewModel: LikeViewModel by viewModels()

    private val binding get() = _binding!!

    private var partner = ""
    private var gender = ""
    private var isHeart = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _binding = DialogLikeBinding.inflate(inflater, container, false)

        binding.tvLike.text = " +${Preferences.remainHeart}   호감이 있어요!"
        binding.tvClose.text = " +${Preferences.remainLike}   친해지고 싶어요!"


        binding.tvLike.setOnClickListener {
            if(Preferences.remainHeart != 0) {
                isHeart = 1
                viewModel.sendHeart(Preferences.userName, partner)
            }
        }

        binding.tvClose.setOnClickListener {
            if(Preferences.remainLike != 0){
                viewModel.sendLike(Preferences.userName, partner)
                isHeart = 2
            }
        }

        initLiveDataObservers()
        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            viewModel.insertMatch(it.matches)
        }
        viewModel.result.observe(viewLifecycleOwner) {
            if(it == "ok"){
                if(isHeart == 1)
                    Preferences.remainHeart = Preferences.remainHeart - 1
                else
                    Preferences.remainLike = Preferences.remainLike - 1

                setFragmentResult("like", Bundle().apply {
                    putInt("isHeart", isHeart)
                })
                findNavController().popBackStack()
            } else {
                showToast("보낼 수 없습니다.")
                findNavController().popBackStack()
            }
        }
    }

    private fun initArgs(){
        arguments?.apply {
            partner = getString("partner", "")
            gender = getString("gender", "")
        }
    }
}