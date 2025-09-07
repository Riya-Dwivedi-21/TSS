// Temporarily disabled for compatibility
/*
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
		// animate views on entry
		val animUp = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_up)
		val animFade = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in)
		binding.tvWelcome.startAnimation(animFade)
		binding.btnNewUser.startAnimation(animUp)

		binding.btnNewUser.setOnClickListener {
			startActivity(Intent(this, TouristRegistrationActivity::class.java))
			finish()
		}

		binding.btnLogin.setOnClickListener {
			startActivity(Intent(this, LoginActivity::class.java))
			finish()
		}
	}
}
*/


