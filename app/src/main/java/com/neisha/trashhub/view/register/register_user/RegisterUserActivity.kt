package com.neisha.trashhub.view.register.register_user

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
import androidx.lifecycle.Observer
import com.neisha.trashhub.R
import com.neisha.trashhub.databinding.ActivityRegisterUserBinding
import com.neisha.trashhub.view.main.LoginActivity
import com.neisha.trashhub.viewmodel.RegisterViewModel
import com.neisha.trashhub.viewmodel.ViewModelFactory

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var loadingIndicator: ProgressBar
    private val viewModel: RegisterViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingIndicator = findViewById(R.id.progressBar)

        setupView()
        setupAction()

        viewModel.loading.observe(this, Observer { isLoading ->
            loadingIndicator.visibility = if (isLoading) ProgressBar.VISIBLE else ProgressBar.GONE
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
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.namaLengkapEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val phone = binding.noPonselEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                viewModel.register(name, email, password, confirmPassword, phone) { response ->
                    if (!response.error) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah")
                            setMessage("User $email Created.")
                            setPositiveButton("Login") { _, _ ->
                                val intent =
                                    Intent(this@RegisterUserActivity, LoginActivity::class.java)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
