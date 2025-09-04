package com.touristsafety.app.ui

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.touristsafety.app.databinding.ActivityTouristRegistrationBinding
import com.touristsafety.app.R
import com.touristsafety.app.ui.viewmodel.TouristRegistrationViewModel

class TouristRegistrationActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTouristRegistrationBinding
    private lateinit var viewModel: TouristRegistrationViewModel
    private var selectedPhotoUri: Uri? = null
    private var hasProfilePhoto: Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTouristRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // simple enter animation
        binding.root.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in))
        
        setupViewModel()
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[TouristRegistrationViewModel::class.java]
    }
    
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            registerTourist()
        }
        // Photo capture trigger (camera only)
        binding.ivProfilePhoto.setOnClickListener { openCamera() }
        binding.btnCaptureFromCamera.setOnClickListener { openCamera() }
    }
    
    private fun observeViewModel() {
        viewModel.registrationState.observe(this) { state ->
            when (state) {
                is TouristRegistrationViewModel.RegistrationState.Loading -> {
                    showLoading(true)
                }
                is TouristRegistrationViewModel.RegistrationState.Success -> {
                    showLoading(false)
                    showSuccessMessage()
                    // Demo: register identifier to password into memory store
                    val identifier = binding.etEmail.text.toString().ifBlank { binding.etPhone.text.toString() }
                    val password = "Password@123" // In a real app, collect a password field
                    if (identifier.isNotBlank()) {
                        InMemoryAuthStore.register(identifier, password)
                    }
                    navigateToQRCode()
                }
                is TouristRegistrationViewModel.RegistrationState.Error -> {
                    showLoading(false)
                    showErrorMessage(state.message)
                }
            }
        }
        
        viewModel.touristId.observe(this) { id ->
            // Store the tourist ID for QR code generation
            // In a real app, you might want to pass this via Intent extras
        }
    }
    
    private fun registerTourist() {
        val name = binding.etName.text.toString().trim()
        // Removed dedicated passport/aadhaar fields; using verification type + number below
        val phoneNumber = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val emergencyContactName = binding.etEmergencyName.text.toString().trim()
        val emergencyContactPhone = binding.etEmergencyPhone.text.toString().trim()
        val emergencyContactRelation = binding.etEmergencyRelation.text.toString().trim()
        val nationality = binding.spinnerNationality.selectedItem?.toString()?.trim() ?: ""
        val docType = binding.spinnerDocType.selectedItem?.toString()?.trim() ?: ""
        val docNumber = binding.etDocNumber.text.toString().trim()
        // Minimal validations
        if (!hasProfilePhoto && binding.ivProfilePhoto.drawable == null) {
            showErrorMessage("Please add a profile photo")
            return
        }
        if (password.length < 8 || !password.any { it.isDigit() } || !password.any { it.isUpperCase() }) {
            showErrorMessage("Password must be 8+ chars with uppercase and number")
            return
        }
        if (password != confirmPassword) {
            showErrorMessage("Passwords do not match")
            return
        }
        if (nationality.isEmpty()) {
            showErrorMessage("Please select nationality")
            return
        }
        if (docType.isEmpty() || docNumber.isEmpty()) {
            showErrorMessage("Please provide document type and number")
            return
        }

        // Simple document validation samples
        val docValid = when (docType) {
            "Aadhaar" -> docNumber.matches(Regex("^[2-9][0-9]{11}$"))
            "Passport" -> docNumber.matches(Regex("^[A-PR-WY][1-9][0-9]{6}$", RegexOption.IGNORE_CASE))
            "Driving Licence" -> docNumber.length in 10..16
            else -> docNumber.length >= 6
        }
        if (!docValid) {
            showErrorMessage("Enter a valid $docType number")
            return
        }
        val hotelName = binding.etHotelName.text.toString().trim()
        val hotelAddress = binding.etHotelAddress.text.toString().trim()
        val tripPurpose = binding.etTripPurpose.text.toString().trim()
        
        viewModel.registerTourist(
            name = name,
            verificationType = docType,
            verificationNumber = docNumber,
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

    override fun onStart() {
        super.onStart()
        // Populate spinners
        val countries = listOf(
            "India", "United States", "United Kingdom", "Canada", "Australia", "Germany", "France", "Japan", "China", "Nepal", "Bhutan", "Bangladesh", "Sri Lanka"
        )
        val docTypes = listOf(
            "Aadhaar", "Passport"
        )
        val itemLayout = R.layout.spinner_item
        val dropLayout = R.layout.spinner_dropdown_item
        binding.spinnerNationality.adapter = ArrayAdapter(this, itemLayout, countries).also { it.setDropDownViewResource(dropLayout) }
        binding.spinnerDocType.adapter = ArrayAdapter(this, itemLayout, docTypes).also { it.setDropDownViewResource(dropLayout) }
    }

    private fun openImagePicker() {
        ensureGalleryPermission()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1001)
    }

    private fun openCamera() {
        ensureCameraPermission()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1002)
    }

    private fun ensureCameraPermission() {
        // Runtime permission simplified for demo
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 2001)
        }
    }

    private fun ensureGalleryPermission() {
        // Handle Android 13+ READ_MEDIA_IMAGES, else READ_EXTERNAL_STORAGE
        val perms = if (android.os.Build.VERSION.SDK_INT >= 33) arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        else arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (perms.any { checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }) {
            requestPermissions(perms, 2002)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            1001 -> {
                selectedPhotoUri = data?.data
                binding.ivProfilePhoto.setImageURI(selectedPhotoUri)
                hasProfilePhoto = selectedPhotoUri != null || binding.ivProfilePhoto.drawable != null
            }
            1002 -> {
                val bitmap = data?.extras?.get("data") as? android.graphics.Bitmap
                if (bitmap != null) {
                    binding.ivProfilePhoto.setImageBitmap(bitmap)
                    hasProfilePhoto = true
                    // Not persisting URI for camera thumbnail in this demo
                }
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }
    
    private fun showSuccessMessage() {
        Toast.makeText(this, "Tourist registered successfully!", Toast.LENGTH_LONG).show()
    }
    
    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun navigateToQRCode() {
        val intent = Intent(this, QRCodeActivity::class.java).apply {
            // Pass the tourist data to QRCodeActivity
            putExtra("tourist_name", binding.etName.text.toString().trim())
            putExtra("tourist_id", viewModel.touristId.value ?: "TOURIST_${System.currentTimeMillis()}")
            putExtra("nationality", binding.spinnerNationality.selectedItem?.toString()?.trim() ?: "")
            putExtra("emergency_contact_name", binding.etEmergencyName.text.toString().trim())
            putExtra("emergency_contact_phone", binding.etEmergencyPhone.text.toString().trim())
            putExtra("verification_type", binding.spinnerDocType.selectedItem?.toString()?.trim() ?: "")
            putExtra("verification_number", binding.etDocNumber.text.toString().trim())
            putExtra("phone_number", binding.etPhone.text.toString().trim())
            putExtra("email", binding.etEmail.text.toString().trim())
            putExtra("hotel_name", binding.etHotelName.text.toString().trim())
            putExtra("hotel_address", binding.etHotelAddress.text.toString().trim())
            putExtra("trip_purpose", binding.etTripPurpose.text.toString().trim())
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_up, R.anim.fade_in)
        finish() // Close this activity
    }
}
