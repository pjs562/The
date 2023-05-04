package unist.pjs.the.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentSignup2Binding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast
import unist.pjs.the.ui.theme.TheTheme

@AndroidEntryPoint
class SignupFragment2 : Fragment() {

    private var _binding: FragmentSignup2Binding? = null

    private val viewModel: SignupFragment2ViewModel by viewModels()

    private val binding get() = _binding!!

    private var gender = ""
    private var name = ""
    private var birthday = ""
    private var group = ""
    private var phoneNumber = ""
    private var auth_no = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCellList()
        initArgs()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignup2Binding.inflate(inflater, container, false)

        binding.btnNext.setOnClickListener {
            if(group.isNotEmpty()){
                viewModel.postRegister(name, birthday, gender, phoneNumber, group, auth_no)
            }else{
                showToast("당신이 속한 목장을 선택해주세요!")
            }
        }

        binding.btnPrev.setOnClickListener {
            findNavController().popBackStack()
        }

        initLiveDataObservers()
        return binding.root
    }

    private fun initLiveDataObservers() {
        viewModel.cellList.observe(viewLifecycleOwner) {
            binding.cvCell.setContent {
                TheTheme {
                    GridCellList(cellList = it, viewModel = viewModel)
                }
            }
        }

        viewModel.group.observe(viewLifecycleOwner){
            group = it
        }

        viewModel.userInfo.observe(viewLifecycleOwner){
            if(!it.error.isNullOrEmpty()){
                showToast(it.error)
            }else{
                Preferences.userName = it.name.toString()
                Preferences.userId = it._id
                viewModel.postToken(Preferences.userId, Preferences.fcmToken)
            }
        }


        viewModel.confirm.observe(viewLifecycleOwner) {
            if(it.ok == "success"){
                val action = SignupFragment2Directions.actionSignupFragment2ToNavigationMain(Preferences.userName)
                findNavController().navigate(action)
            }
            else
                showToast(it.error)
        }
    }

    private fun initArgs(){
        arguments?.apply {
            name = getString("name", "")
            gender = getString("gender", "")
            birthday = getString("birthday", "")
            phoneNumber = getString("phoneNumber", "")
            auth_no = getString("auth_no", "")
        }
    }
}