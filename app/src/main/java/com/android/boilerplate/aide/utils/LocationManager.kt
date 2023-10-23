package com.android.boilerplate.aide.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class LocationManager @Inject constructor(@ApplicationContext private val context: Context) {

    val currentLocation = MutableLiveData<Location>(null)
    val requestLocationPermission = MutableLiveData(false)
    val satisfyLocationSettings = MutableLiveData(false)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun initLocationFlow() {
        if (Utils.isMPlus()) {
            if (checkLocationPermission()) {
                satisfyLocationSettings()
            } else {
                requestLocationPermission()
            }
        } else {
            satisfyLocationSettings()
        }
    }

    fun resetRequestLocationPermission() {
        requestLocationPermission.value = false
    }

    fun resetSatisfyLocationSettings() {
        satisfyLocationSettings.value = false
    }

    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(5000).apply {
            setMinUpdateIntervalMillis(5000)
            setMinUpdateDistanceMeters(5f)
            setWaitForAccurateLocation(true)
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        }.build()
        if (checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun checkLocationPermission(): Boolean {
        val p1 = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val p2 = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return p1 == PackageManager.PERMISSION_GRANTED && p2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestLocationPermission.value = true
    }

    private fun satisfyLocationSettings() {
        satisfyLocationSettings.value = true
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            currentLocation.value = locationResult.locations[0]
        }
    }
}