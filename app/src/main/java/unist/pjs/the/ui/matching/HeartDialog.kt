package unist.pjs.the.ui.matching

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import unist.pjs.the.databinding.DialogHeartBinding
import unist.pjs.the.extends.Preferences

class HeartDialog: DialogFragment() {

    private var _binding: DialogHeartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        _binding = DialogHeartBinding.inflate(inflater, container, false)

        binding.tvClose.text = "  +${Preferences.remainLike}"
        binding.tvLike.text = "  +${Preferences.remainHeart}"
        return binding.root
    }
}