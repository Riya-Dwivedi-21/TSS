# Tourist Safety System - Android App

## Project Overview

This Android application is part of a Smart Tourist Safety Monitoring & Incident Response System that leverages AI, Blockchain, and Geo-Fencing technologies. The app provides a secure digital ID generation platform for tourists with real-time safety monitoring capabilities.

## Features Implemented

### 1. Tourist Registration Screen
- **Personal Information**: Name, Passport/Aadhaar number, Phone, Email, Nationality
- **Emergency Contact**: Contact name, phone number, and relationship
- **Trip Information**: Hotel details, address, and trip purpose
- **Form Validation**: Real-time validation with proper error handling
- **Data Persistence**: Room database integration for local storage

### 2. QR Code Generation & Display
- **Dynamic QR Code**: Generates QR codes with comprehensive tourist information
- **Tourist ID Card**: Displays formatted tourist information with safety score
- **Share Functionality**: Share tourist ID via other apps
- **Download Feature**: Save tourist ID for offline access

### 3. Technical Architecture
- **MVVM Pattern**: Clean architecture with ViewModel and Repository pattern
- **Room Database**: Local data persistence with SQLite
- **Material Design**: Modern UI with Material Design components
- **Data Binding**: Efficient UI updates with data binding
- **Coroutines**: Asynchronous operations for better performance

## Technology Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **UI**: Material Design Components
- **QR Code**: ZXing library
- **Async Operations**: Kotlin Coroutines
- **Data Binding**: Android Data Binding

## Project Structure

```
app/src/main/java/com/touristsafety/app/
├── data/
│   ├── dao/           # Data Access Objects
│   ├── database/      # Room database setup
│   ├── model/         # Data models/entities
│   ├── repository/    # Repository pattern implementation
│   └── util/          # Utility classes (DateConverter)
├── ui/
│   ├── viewmodel/     # ViewModels
│   ├── MainActivity.kt
│   ├── TouristRegistrationActivity.kt
│   └── QRCodeActivity.kt
└── TouristSafetyApplication.kt
```

## Setup Instructions

1. **Clone the repository**
2. **Open in Android Studio**
3. **Sync Gradle files**
4. **Build and run the application**

## Usage

1. **Launch the app** - Main screen with two options
2. **Register Tourist** - Fill in tourist details and submit
3. **View QR Code** - Display generated tourist ID with QR code
4. **Share/Download** - Share or save the tourist ID

## Database Schema

### Tourist Table
- `id` (Primary Key): Unique tourist identifier
- `name`: Full name of the tourist
- `passportNumber`: Passport number
- `aadhaarNumber`: Aadhaar number
- `phoneNumber`: Contact phone number
- `email`: Email address
- `emergencyContactName`: Emergency contact name
- `emergencyContactPhone`: Emergency contact phone
- `emergencyContactRelation`: Relationship with emergency contact
- `nationality`: Tourist nationality
- `hotelName`: Hotel name
- `hotelAddress`: Hotel address
- `tripPurpose`: Purpose of the trip
- `safetyScore`: Safety score (0-100)
- `isActive`: Active status
- `createdAt`: Registration timestamp
- `lastUpdated`: Last update timestamp

## Future Enhancements

- **Blockchain Integration**: Secure digital ID verification
- **Geo-Fencing**: Location-based safety alerts
- **AI Anomaly Detection**: Behavioral pattern analysis
- **Real-time Tracking**: GPS-based location monitoring
- **Emergency Alerts**: Panic button functionality
- **Multi-language Support**: 10+ Indian languages
- **IoT Integration**: Smart wearable devices
- **Police Dashboard**: Law enforcement interface

## Contributing

This project is part of a 6-member team development. Each member has specific responsibilities:

- **UI/UX (View)**: Android app development and user interface
- **Backend**: API development and server-side logic
- **AI/ML**: Anomaly detection and predictive analytics
- **Blockchain**: Digital ID verification and security
- **DevOps**: Deployment and infrastructure
- **Testing**: Quality assurance and testing

## License

© 2024 Tourist Safety System. All rights reserved.
