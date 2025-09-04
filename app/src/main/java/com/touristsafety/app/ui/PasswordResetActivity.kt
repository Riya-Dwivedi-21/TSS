package com.touristsafety.app.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.touristsafety.app.databinding.ActivityPasswordResetBinding

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReset.setOnClickListener {
            val identifier = binding.etIdentifier.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            if (identifier.isEmpty()) {
                Toast.makeText(this, "Enter your email or phone", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (newPassword.length < 8 || !newPassword.any { it.isDigit() } || !newPassword.any { it.isUpperCase() }) {
                Toast.makeText(this, "Weak password: 8+ chars, uppercase and number", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            InMemoryAuthStore.register(identifier, newPassword)
            Toast.makeText(this, "Password reset successfully", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}



