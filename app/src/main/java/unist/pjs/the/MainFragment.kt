package unist.pjs.the

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.databinding.FragmentMainBinding
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.showToast

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
        viewModel.getUserInfo(Preferences.userName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val navController =
            NavHostFragment.findNavController(childFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_board -> showBottomNav(true)
                R.id.navigation_message -> showBottomNav(true)
                R.id.navigation_matching -> showBottomNav(true)
                R.id.navigation_profile -> showBottomNav(true)
                else -> showBottomNav(false)
            }
        }

        binding.navView.setupWithNavController(navController)

        binding.navView.selectedItemId = R.id.navigation_matching

        initLiveDataObservers()

        return binding.root
    }

    private fun showBottomNav(state: Boolean){
        if(state) binding.navView.visibility = View.VISIBLE else binding.navView.visibility = View.GONE
    }

    private fun initArgs(){
        arguments?.apply {
            name = getString("name", "")
        }
    }

    private fun initLiveDataObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) { it ->
            if (it.name.isNullOrEmpty()) {
                showToast("잘못된 정보를 입력했습니다.")
            } else {
                it.likedBoards?.let { it1 -> viewModel.insertLikeBoards(it1) }
                viewModel.insertMatches(it.matches)
                it.rooms?.let { list -> viewModel.insertRooms(list) }
                Preferences.userId = it._id
                Preferences.userName = it.name
                Preferences.userAge = it.age
                Preferences.userImage = if(it.imageUrls?.isNotEmpty() == true) it.imageUrls[0] else ""
                Preferences.userBio = if(it.bio?.isNotEmpty() == true) it.bio else ""
                Preferences.remainHeart = it.remainHeart
                Preferences.remainLike = it.remainLike
                Preferences.userGroup = it.group
                Preferences.userGender = it.gender
                Preferences.userAdmin = it.admin
            }
        }
    }
}