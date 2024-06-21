package com.neisha.trashhub.view.register.register_mitra

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
import com.neisha.trashhub.databinding.ActivityRegisterMitraBinding

import com.neisha.trashhub.view.main.LoginActivity
import com.neisha.trashhub.viewmodel.RegisterViewModel
import com.neisha.trashhub.viewmodel.ViewModelFactory

class RegisterMitraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterMitraBinding
    private lateinit var loadingIndicator: ProgressBar
    private val viewModel: RegisterViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMitraBinding.inflate(layoutInflater)
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
            val asalMitra = binding.asalMitraEditText.text.toString()
            val namaLengkap = binding.namaLengkapEditText.text.toString()
            val noPonsel = binding.noPonselEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (asalMitra.isNotEmpty() && namaLengkap.isNotEmpty() && noPonsel.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                viewModel.register(
                    namaLengkap,
                    email,
                    password,
                    confirmPassword,
                    noPonsel,
                    asalMitra
                ) { response ->
                    if (!response.error) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah")
                            setMessage("User $email Created.")
                            setPositiveButton("Login") { _, _ ->
                                val intent = Intent(
                                    this@RegisterMitraActivity,
                                    LoginActivity::class.java
                                )
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