package unist.pjs.the.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.R
import unist.pjs.the.databinding.FragmentMessageBinding
import unist.pjs.the.databinding.FragmentPrivacyBinding

@AndroidEntryPoint
class PrivacyFragment : Fragment() {

    private var _binding: FragmentPrivacyBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPrivacyBinding.inflate(inflater, container, false)

        binding.tvDone.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
}