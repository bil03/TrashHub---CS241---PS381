package com.neisha.trashhub.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.neisha.trashhub.data.pref.UserModel
import com.neisha.trashhub.data.pref.UserPreference
import com.neisha.trashhub.data.pref.dataStore
import com.neisha.trashhub.data.pref.response.LoginResponse
import com.neisha.trashhub.databinding.ActivityLoginBinding
import com.neisha.trashhub.view.main.MainActivity
import com.neisha.trashhub.viewmodel.LoginViewModel
import com.neisha.trashhub.viewmodel.ViewModelFactory
import java.net.HttpURLConnection

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var userPreference: UserPreference
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore = applicationContext.dataStore // Use applicationContext to get DataStore

        userPreference = UserPreference.getInstance(dataStore)

        setupView()
        setupAction()
        populateEmailFromIntent()

        viewModel.loading.observe(this) { isLoading ->
            loadingIndicator.isVisible = isLoading
        }

        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { response: LoginResponse ->
                if (!response.error) {
                    response.loginResult?.let { loginResult ->
                        val userModel = UserModel(
                            id = loginResult.id,
                            name = loginResult.name,
                            email = loginResult.email,
                            token = loginResult.token,
                            phone = "",
                            roles = loginResult.roles,
                            isLogin = true
                        )
                        viewModel.saveSession(userModel)
                        showSuccessDialog()
                    }
                } else {
                    handleLoginError(response.message ?: "Login gagal")
                }
            }
            result.onFailure { error ->
                handleLoginError(error.message ?: "Unknown error")
            }
        }

        userPreference.getSession().asLiveData().observe(this, Observer { user ->
            user?.let {
                if (it.isLogin) {
                    navigateToMainActivity()
                }
            }
        })
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        loadingIndicator = binding.progressBar
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLogin(email, password)
            } else {
                showToast("Email dan password harus diisi")
            }
        }

        binding.registerTextView.setOnClickListener {
            // Implement your registration logic here
        }
    }

    private fun performLogin(email: String, password: String) {
        viewModel.login(email, password)
    }

    private fun populateEmailFromIntent() {
        val email = intent.getStringExtra("email")
        email?.let {
            binding.emailEditText.setText(it)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showSuccessDialog() {
        if (!isFinishing) {
            AlertDialog.Builder(this).apply {
                setTitle("Success!")
                setMessage("Login successful")
                setPositiveButton("Continue") { _, _ ->
                    navigateToMainActivity()
                }
                create().apply {
                    setCanceledOnTouchOutside(false)
                    setCancelable(false)
                    show()
                }
            }
        }
    }

    private fun handleLoginError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
