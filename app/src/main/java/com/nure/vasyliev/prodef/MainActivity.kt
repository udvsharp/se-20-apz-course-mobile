package com.nure.vasyliev.prodef

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nure.vasyliev.prodef.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_pomodoro -> {
                    navController.navigate(R.id.pomodoroFragment)
                    true
                }
                R.id.navigation_sessions -> {
                    navController.navigate(R.id.sessionsFragment)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> {
                    showBottomBar(false)
                    supportActionBar?.hide()
                }
                R.id.signInFragment, R.id.signUpFragment -> {
                    showBottomBar(false)
                    supportActionBar?.show()
                }
                else -> {
                    showBottomBar(true)
                    supportActionBar?.show()
                }
            }
        }
    }

    private fun showBottomBar(show: Boolean) {
        binding.navView.isVisible = show
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController(R.id.container).navigateUp()
            }
            else -> {}
        }
        return true
    }
}