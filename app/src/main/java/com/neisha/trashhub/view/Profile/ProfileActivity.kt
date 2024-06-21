package com.neisha.trashhub.view.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.neisha.trashhub.R
import com.neisha.trashhub.view.articles.ArticlesActivity
import com.neisha.trashhub.view.main.MainActivity
import com.neisha.trashhub.view.splash.FirstActivity
import com.neisha.trashhub.viewmodel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_news -> {
                    val intent = Intent(this, ArticlesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_account -> {
                    // Handle account action
                    true
                }
                else -> false
            }
        }

        // Observe session data
        viewModel.getSession().observe(this) { user ->
            user?.let {
                // Update UI with user data
                // Example: findViewById<TextView>(R.id.profile_name).text = it.name
            }
        }
    }

    fun onEditProfileClick(view: View) {
        // Handle edit profile action
    }

    fun onLogoutClick(view: View) {
        Log.d("ProfileActivity", "Logout button clicked")
        showLogoutConfirmationDialog()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logoutUser() {
        viewModel.logout {
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
            navigateToFirstActivity()
        }
    }

    private fun navigateToFirstActivity() {
        val intent = Intent(this, FirstActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finishAffinity()
    }
}
