package com.touristsafety.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.touristsafety.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLaunchComposeRegistration.setOnClickListener {
            val intent = Intent(this, com.touristsafety.app.ui.compose.SimpleTSSRegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}