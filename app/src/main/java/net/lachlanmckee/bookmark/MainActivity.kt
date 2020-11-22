package net.lachlanmckee.bookmark

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.main_activity)

    val host: NavHostFragment = supportFragmentManager
      .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

    val navController = host.navController
    setupBottomNavMenu(navController)
    setupNavigationVisibilityToggle(navController)
  }

  private fun setupBottomNavMenu(navController: NavController) {
    findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(navController)
  }

  private fun setupNavigationVisibilityToggle(navController: NavController) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
      findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = when (destination.id) {
        R.id.home_dest,
        R.id.settings_dest -> View.VISIBLE
        else -> View.GONE
      }
    }
  }
}
