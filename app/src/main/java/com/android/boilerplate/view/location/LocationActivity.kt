package com.android.boilerplate.view.location

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.android.boilerplate.R
import com.android.boilerplate.aide.utils.LocationManager
import com.android.boilerplate.base.view.BaseActivity
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.ActivityLocationBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class LocationActivity : BaseActivity() {

    @Inject
    lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityLocationBinding

    override fun getViewModel(): BaseViewModel? = null

    override fun hasConnectivity(connectivity: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location)
        locationManager.apply {
            requestLocationPermission.observe(this@LocationActivity) {
                it?.let {
                    if (it) {
                        requestLocationPermission()
                        // location permission has been requested, reset flag in LocationManager
                        resetRequestLocationPermission()
                    }
                }
            }
            satisfyLocationSettings.observe(this@LocationActivity) {
                it?.let {
                    if (it) {
                        satisfyLocationSettings()
                        // location settings has been invoked, reset flag in LocationManager
                        resetSatisfyLocationSettings()
                    }
                }
            }
            currentLocation.observe(this@LocationActivity) {
                it?.let {
                    // updated device location
                }
            }
            initLocationFlow()
        }
    }

    override fun onDestroy() {
        locationManager.stopLocationUpdates()
        super.onDestroy()
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (!it.values.contains(false)) {
                satisfyLocationSettings()
            } else {
                showToast(getString(R.string.l_p_not_granted))
            }
        }

    private fun satisfyLocationSettings() {
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(
            LocationSettingsRequest
                .Builder()
                .addLocationRequest(LocationRequest.Builder(5000).apply {
                    setMinUpdateIntervalMillis(5000)
                    setMinUpdateDistanceMeters(5f)
                    setWaitForAccurateLocation(true)
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                }.build())
                .build()
        ).apply {
            addOnSuccessListener {
                // all location settings are satisfied, the client can initialize location
                // requests here
                locationManager.startLocationUpdates()
            }
            addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // location settings are not satisfied but this can be fixed by showing the
                    // user a dialog.
                    try {
                        // show the dialog by calling startResolutionForResult() and check the
                        // result in onActivityResult().
                        locationResolutionRequest.launch(
                            IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.e(TAG, sendEx.toString())
                    }
                }
            }
        }
    }

    private val locationResolutionRequest = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "location settings satisfied")
            locationManager.startLocationUpdates()
        } else {
            Log.i(TAG, "location settings not satisfied")
        }
    }

    companion object {
        private val TAG = LocationActivity::class.java.simpleName
    }
}