package com.touristsafety.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.touristsafety.app.databinding.ActivityChoiceBinding
import com.touristsafety.app.R

class ChoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // animate buttons on entry
        val anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.btnExistingUser.startAnimation(anim)
        binding.btnNewUser.startAnimation(anim)

        binding.btnExistingUser.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnNewUser.setOnClickListener {
            startActivity(Intent(this, TouristRegistrationActivity::class.java))
        }
    }
}


