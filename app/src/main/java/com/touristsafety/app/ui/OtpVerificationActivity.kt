package com.touristsafety.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.touristsafety.app.R
import com.touristsafety.app.databinding.ActivityOtpVerificationBinding
import com.touristsafety.app.ui.viewmodel.TouristRegistrationViewModel

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var viewModel: TouristRegistrationViewModel

    private var generatedOtp: String = ""
    private var expiryAtMs: Long = 0L
    private var countDownTimer: CountDownTimer? = null
    private val smsPermissionRequest = 6001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TouristRegistrationViewModel::class.java]

        val phone = intent.getStringExtra("phoneNumber").orEmpty()
        binding.tvSubtitle.text = getString(R.string.otp_sent_to, phone)

        generateAndSendOtp(phone)
        startResendCountdown(60)

        binding.btnResend.setOnClickListener {
            generateAndSendOtp(phone)
            startResendCountdown(60)
        }

        binding.btnVerify.setOnClickListener {
            val entered = binding.etOtp.text?.toString()?.trim().orEmpty()
            val now = System.currentTimeMillis()
            if (entered == generatedOtp && now <= expiryAtMs) {
                performRegistration()
            } else {
                Toast.makeText(this, getString(R.string.otp_invalid), Toast.LENGTH_LONG).show()
            }
        }

        observeRegistration()
    }

    private fun observeRegistration() {
        viewModel.registrationState.observe(this) { state ->
            when (state) {
                is TouristRegistrationViewModel.RegistrationState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is TouristRegistrationViewModel.RegistrationState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    // Proceed to QR page
                    val intent = android.content.Intent(this, QRCodeActivity::class.java).apply {
                        putExtra("tourist_name", intent.getStringExtra("name"))
                        putExtra("tourist_id", viewModel.touristId.value ?: "TOURIST_${System.currentTimeMillis()}")
                        putExtra("nationality", intent.getStringExtra("nationality"))
                        putExtra("emergency_contact_name", intent.getStringExtra("emergencyContactName"))
                        putExtra("emergency_contact_phone", intent.getStringExtra("emergencyContactPhone"))
                        putExtra("verification_type", intent.getStringExtra("docType"))
                        putExtra("verification_number", intent.getStringExtra("docNumber"))
                        putExtra("phone_number", intent.getStringExtra("phoneNumber"))
                        putExtra("email", intent.getStringExtra("email"))
                        putExtra("hotel_name", intent.getStringExtra("hotelName"))
                        putExtra("hotel_address", intent.getStringExtra("hotelAddress"))
                        putExtra("trip_purpose", intent.getStringExtra("tripPurpose"))
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_up, R.anim.fade_in)
                    finish()
                }
                is TouristRegistrationViewModel.RegistrationState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun performRegistration() {
        val name = intent.getStringExtra("name").orEmpty()
        val verificationType = intent.getStringExtra("docType").orEmpty()
        val verificationNumber = intent.getStringExtra("docNumber").orEmpty()
        val phoneNumber = intent.getStringExtra("phoneNumber").orEmpty()
        val email = intent.getStringExtra("email").orEmpty()
        val emergencyContactName = intent.getStringExtra("emergencyContactName").orEmpty()
        val emergencyContactPhone = intent.getStringExtra("emergencyContactPhone").orEmpty()
        val emergencyContactRelation = intent.getStringExtra("emergencyContactRelation").orEmpty()
        val nationality = intent.getStringExtra("nationality").orEmpty()
        val hotelName = intent.getStringExtra("hotelName").orEmpty()
        val hotelAddress = intent.getStringExtra("hotelAddress").orEmpty()
        val tripPurpose = intent.getStringExtra("tripPurpose").orEmpty()

        viewModel.registerTourist(
            name = name,
            verificationType = verificationType,
            verificationNumber = verificationNumber,
            phoneNumber = phoneNumber,
            email = email,
            emergencyContactName = emergencyContactName,
            emergencyContactPhone = emergencyContactPhone,
            emergencyContactRelation = emergencyContactRelation,
            nationality = nationality,
            hotelName = hotelName,
            hotelAddress = hotelAddress,
            tripPurpose = tripPurpose
        )
    }

    private fun generateAndSendOtp(phone: String) {
        generatedOtp = (100000..999999).random().toString()
        expiryAtMs = System.currentTimeMillis() + 60_000
        ensureSmsPermissionAndSend(phone, "Your TSS OTP is $generatedOtp. It is valid for 1 minute.")
        Toast.makeText(this, getString(R.string.otp_sent), Toast.LENGTH_SHORT).show()
    }

    private fun ensureSmsPermissionAndSend(phone: String, message: String) {
        val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), smsPermissionRequest)
            return
        }
        try {
            SmsManager.getDefault().sendTextMessage(phone, null, message, null, null)
        } catch (_: Exception) {
            // In real-world, use server-based OTP sending for reliability
        }
    }

    private fun startResendCountdown(seconds: Int) {
        binding.btnResend.isEnabled = false
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val s = (millisUntilFinished / 1000L).toInt()
                binding.tvResendTimer.text = getString(R.string.otp_resend_in, s)
            }
            override fun onFinish() {
                binding.tvResendTimer.text = ""
                binding.btnResend.isEnabled = true
            }
        }.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == smsPermissionRequest) {
            // No immediate resend; user will tap resend again
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}


