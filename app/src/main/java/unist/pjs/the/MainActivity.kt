package unist.pjs.the

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import unist.pjs.the.extends.Preferences

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_main_host) as NavHostFragment
        navController = navHostFragment.navController

        if (Preferences.userId.isEmpty()) {
            goFragment(R.id.action_navigation_splash_to_loginFragment)
        } else {
            goFragment(R.id.action_navigation_splash_to_navigation_main)
        }

    }

    private fun goFragment(@IdRes id: Int) = navController?.navigate(id)

    override fun onBackPressed() {
        when (navController?.currentDestination?.id) {

            R.id.loginFragment -> {
                finish()
            }

            else -> {
                super.onBackPressed()
            }
        }
    }
}