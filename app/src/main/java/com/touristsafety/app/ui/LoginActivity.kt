package com.touristsafety.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.touristsafety.app.data.model.AuthResult
import com.touristsafety.app.databinding.ActivityLoginBinding
import com.touristsafety.app.ui.viewmodel.AuthViewModel
import com.touristsafety.app.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        authViewModel = ViewModelProvider(this, ViewModelFactory(this))[AuthViewModel::class.java]
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etIdentifier.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            
            authViewModel.login(email, password)
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, PasswordResetActivity::class.java))
        }
    }

    private fun observeViewModel() {
        authViewModel.authState.observe(this) { result ->
            when (result) {
                is AuthResult.Loading -> {
                    showLoading(true)
                }
                is AuthResult.Success -> {
                    showLoading(false)
                    if (result.user.id.isNotEmpty()) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
                is AuthResult.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.btnLogin.isEnabled = !show
        binding.btnLogin.text = if (show) "Logging in..." else "Login"
    }
}


