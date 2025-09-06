package com.touristsafety.app.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.touristsafety.app.databinding.ActivityQrCodeBinding
import com.touristsafety.app.R
import java.io.File
import java.io.FileOutputStream

class QRCodeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityQrCodeBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Start with enhanced entrance animation
        binding.root.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_up))

        setupTouristData()
        generateQRCode()
        setupClickListeners()
        startAnimations()
    }
    
    private fun setupTouristData() {
        // Get tourist data from Intent extras
        val touristName = intent.getStringExtra("tourist_name") ?: "John Doe"
        val touristId = intent.getStringExtra("tourist_id") ?: "TOURIST_${System.currentTimeMillis()}"
        val nationality = intent.getStringExtra("nationality") ?: "Indian"
        val emergencyContactName = intent.getStringExtra("emergency_contact_name") ?: "Jane Doe"
        val emergencyContactPhone = intent.getStringExtra("emergency_contact_phone") ?: "+91 98765 43210"
        
        // Update UI with real tourist data
        binding.tvTouristName.text = touristName
        binding.tvTouristId.text = "Tourist ID: $touristId"
        binding.tvNationality.text = "Nationality: $nationality"
        binding.tvSafetyScore.text = "Safety Score: 95/100"
        binding.tvEmergencyContact.text = "Emergency Contact: $emergencyContactName ($emergencyContactPhone)"
    }
    
    private fun generateQRCode() {
        try {
            // Get tourist data for QR code generation
            val touristName = intent.getStringExtra("tourist_name") ?: "John Doe"
            val touristId = intent.getStringExtra("tourist_id") ?: "TOURIST_${System.currentTimeMillis()}"
            val nationality = intent.getStringExtra("nationality") ?: "Indian"
            val emergencyContactName = intent.getStringExtra("emergency_contact_name") ?: "Jane Doe"
            val emergencyContactPhone = intent.getStringExtra("emergency_contact_phone") ?: "+91 98765 43210"
            val verificationType = intent.getStringExtra("verification_type") ?: ""
            val verificationNumber = intent.getStringExtra("verification_number") ?: ""
            val phoneNumber = intent.getStringExtra("phone_number") ?: ""
            val email = intent.getStringExtra("email") ?: ""
            val hotelName = intent.getStringExtra("hotel_name") ?: ""
            val hotelAddress = intent.getStringExtra("hotel_address") ?: ""
            val tripPurpose = intent.getStringExtra("trip_purpose") ?: ""

            // Generate QR code with comprehensive tourist information
            val touristData = """
                TOURIST SAFETY SYSTEM - DIGITAL ID
                
                Tourist ID: $touristId
                Name: $touristName
                Nationality: $nationality
                $verificationType: $verificationNumber
                Phone: $phoneNumber
                Email: $email
                Hotel: $hotelName
                Address: $hotelAddress
                Purpose: $tripPurpose
                Emergency Contact: $emergencyContactName ($emergencyContactPhone)
                Safety Score: 95/100
                Generated: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}
                
                Scan this QR code to verify tourist identity and access safety information.
            """.trimIndent()
            
            val qrCodeBitmap = generateQRCodeBitmap(touristData, 400, 400)
            binding.ivQRCode.setImageBitmap(qrCodeBitmap)
            
        } catch (e: Exception) {
            // If QR generation fails, show a placeholder
            binding.ivQRCode.setImageResource(android.R.drawable.ic_menu_gallery)
            Toast.makeText(this, "QR code generation failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun generateQRCodeBitmap(content: String, width: Int, height: Int): Bitmap {
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.MARGIN, 1)
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
        }
        
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bits[x, y]) Color.BLACK else Color.WHITE
            }
        }
        
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        
        return bitmap
    }
    
    private fun setupClickListeners() {
        binding.btnContinueToApp.setOnClickListener {
            navigateToMainApp()
        }
        
        binding.btnShare.setOnClickListener {
            shareTouristID()
        }
        
        binding.btnDownload.setOnClickListener {
            downloadTouristID()
        }
    }
    
    private fun navigateToMainApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
    
    private fun shareTouristID() {
        try {
            val cacheDir = File(cacheDir, "qr_share")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            val bitmap = (binding.ivQRCode.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
                ?: run {
                    Toast.makeText(this, "QR not ready", Toast.LENGTH_SHORT).show(); return
                }
            val outFile = File(cacheDir, "tourist_qr_${'$'}{System.currentTimeMillis()}.png")
            FileOutputStream(outFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            val uri: Uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", outFile)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to share tourist ID", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun downloadTouristID() {
        try {
            val pictures = File(getExternalFilesDir(null), "qrcodes")
            if (!pictures.exists()) pictures.mkdirs()
            val bitmap = (binding.ivQRCode.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
                ?: run {
                    Toast.makeText(this, "QR not ready", Toast.LENGTH_SHORT).show(); return
                }
            val outFile = File(pictures, "tourist_qr_${'$'}{System.currentTimeMillis()}.png")
            FileOutputStream(outFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            Toast.makeText(this, "Saved: ${'$'}{outFile.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save QR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startAnimations() {
        // Animate QR code with rotation
        binding.ivQRCode.alpha = 0f
        binding.ivQRCode.rotation = -90f
        binding.ivQRCode.animate()
            .alpha(1f)
            .rotation(0f)
            .setDuration(800)
            .setStartDelay(200)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()
        
        // Animate tourist info with slide in from left
        val infoViews = listOf(
            binding.tvTouristName,
            binding.tvTouristId,
            binding.tvNationality,
            binding.tvSafetyScore,
            binding.tvEmergencyContact
        )
        
        infoViews.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationX = -100f
            view.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(500)
                .setStartDelay((index * 100 + 400).toLong())
                .start()
        }
        
        // Animate buttons with staggered entrance
        binding.btnContinueToApp.alpha = 0f
        binding.btnContinueToApp.translationY = 50f
        binding.btnContinueToApp.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(800)
            .start()
        
        binding.btnShare.alpha = 0f
        binding.btnShare.translationY = 50f
        binding.btnShare.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(900)
            .start()
        
        binding.btnDownload.alpha = 0f
        binding.btnDownload.translationY = 50f
        binding.btnDownload.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(1000)
            .start()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out)
    }
}
