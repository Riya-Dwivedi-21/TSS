package com.touristsafety.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.touristsafety.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val identifier = binding.etIdentifier.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email/phone and password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val success = InMemoryAuthStore.login(identifier, password)
            if (success) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                // Go to home
                startActivity(android.content.Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(android.content.Intent(this, PasswordResetActivity::class.java))
        }
    }
}


