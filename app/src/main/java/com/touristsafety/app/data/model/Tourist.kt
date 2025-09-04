package com.touristsafety.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Tourist(
    val id: String = "",
    val name: String = "",
    val verificationType: String = "", // e.g., Aadhaar, Passport
    val verificationNumber: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val emergencyContactRelation: String = "",
    val dateOfBirth: Date? = null,
    val nationality: String = "",
    val arrivalDate: Date? = null,
    val departureDate: Date? = null,
    val hotelName: String = "",
    val hotelAddress: String = "",
    val tripPurpose: String = "",
    val safetyScore: Int = 100,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val lastUpdated: Date = Date()
) : Parcelable
