package com.itis.foody

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.foody.common.extensions.findController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var controller: NavController
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Foody)
        setContentView(R.layout.activity_main)

        controller = findController(R.id.container)

        setUpNav()
    }

    private fun setUpNav() {
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setupWithNavController(controller)

        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment -> hideBottomNav()
                R.id.loginFragment -> hideBottomNav()
                R.id.registrationFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE
    }
}
