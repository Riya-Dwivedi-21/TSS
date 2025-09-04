package com.touristsafety.app.ui

import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.touristsafety.app.databinding.ActivityMainBinding
import com.touristsafety.app.R

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val locationRequestCode = 501
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ensureLocationEnabledAndProceed()
    }
    
    private fun ensureLocationEnabledAndProceed() {
        val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), locationRequestCode)
            return
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            // Prompt user to enable GPS like Amazon does
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            // After returning, we continue in onResume
            return
        }
        proceedToApp()
    }

    override fun onResume() {
        super.onResume()
        // If user came back from settings, try again
        if (!isFinishing) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (gpsEnabled && hasPermission) {
                proceedToApp()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (granted) {
                ensureLocationEnabledAndProceed()
            } else {
                // Continue but you might show a rationale or limited experience
                proceedToApp()
            }
        }
    }

    private fun proceedToApp() {
        startActivity(Intent(this, ChoiceActivity::class.java))
        overridePendingTransition(R.anim.slide_up, R.anim.fade_in)
        finish()
    }
}
