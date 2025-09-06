package com.touristsafety.app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touristsafety.app.data.model.Tourist
import com.touristsafety.app.data.repository.TouristRepository
import com.touristsafety.app.data.repository.EncryptedTouristRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.Result

class TouristRegistrationViewModel(private val context: Context) : ViewModel() {
    
    private val repository: TouristRepository = EncryptedTouristRepository(context)
    private val aadhaarVerifier: AadhaarVerifier = AadhaarVerifierImpl()
    
    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: LiveData<RegistrationState> = _registrationState
    
    private val _touristId = MutableLiveData<String>()
    val touristId: LiveData<String> = _touristId
    
    fun registerTourist(
        name: String,
        verificationType: String,
        verificationNumber: String,
        phoneNumber: String,
        email: String,
        emergencyContactName: String,
        emergencyContactPhone: String,
        emergencyContactRelation: String,
        nationality: String,
        hotelName: String,
        hotelAddress: String,
        tripPurpose: String,
        tripDays: Int
    ) {
        if (!validateInputs(name, verificationType, verificationNumber, phoneNumber, email)) {
            _registrationState.value = RegistrationState.Error("Please fill all required fields correctly")
            return
        }
        
        _registrationState.value = RegistrationState.Loading
        
        val tourist = Tourist(
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
            tripPurpose = tripPurpose,
            tripDays = tripDays,
            arrivalDate = Date()
        )
        
        viewModelScope.launch {
            try {
                if (verificationType.equals("Aadhaar", ignoreCase = true)) {
                    val nameMatch = withContext(Dispatchers.IO) {
                        aadhaarVerifier.verifyNameMatch(verificationNumber, name)
                    }
                    if (nameMatch.isFailure || nameMatch.getOrNull() != true) {
                        _registrationState.value = RegistrationState.Error("Aadhaar name does not match")
                        return@launch
                    }
                }
                val result = repository.registerTourist(tourist)
                result.fold(
                    onSuccess = { id ->
                        _touristId.value = id
                        _registrationState.value = RegistrationState.Success
                    },
                    onFailure = { exception ->
                        _registrationState.value = RegistrationState.Error(exception.message ?: "Registration failed")
                    }
                )
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    private fun validateInputs(
        name: String,
        verificationType: String,
        verificationNumber: String,
        phoneNumber: String,
        email: String
    ): Boolean {
        val isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPhoneValid = isValidPhone(phoneNumber)
        val isDocValid = verificationType.isNotBlank() && verificationNumber.isNotBlank()
        return name.isNotBlank() && isDocValid && isPhoneValid && isEmailValid
    }
    
    private fun isValidPhone(phone: String): Boolean {
        val cleaned = phone.replace(" ", "").replace("-", "")
        val matchesPattern = android.util.Patterns.PHONE.matcher(cleaned).matches()
        val lengthOk = cleaned.length in 10..15
        val indianOk = cleaned.length == 10 && cleaned.all { it.isDigit() } && cleaned.first() in '6'..'9'
        return (matchesPattern && lengthOk) || indianOk
    }
    
    sealed class RegistrationState {
        object Loading : RegistrationState()
        object Success : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }
}

// Simple KYC verifier stub to simulate real-world Aadhaar name verification.
// In production, this would call a secure backend that integrates with an approved KUA/OSP.
interface AadhaarVerifier {
    suspend fun verifyNameMatch(aadhaarNumber: String, fullName: String): Result<Boolean>
}

class AadhaarVerifierImpl : AadhaarVerifier {
    override suspend fun verifyNameMatch(aadhaarNumber: String, fullName: String): Result<Boolean> {
        // Simulated remote check with basic format validation; always returns success if formats are plausible
        val aadhaarOk = aadhaarNumber.matches(Regex("^[2-9][0-9]{11}$"))
        val nameOk = fullName.trim().split(" ").size >= 2
        return if (aadhaarOk && nameOk) Result.success(true) else Result.success(false)
    }
}
