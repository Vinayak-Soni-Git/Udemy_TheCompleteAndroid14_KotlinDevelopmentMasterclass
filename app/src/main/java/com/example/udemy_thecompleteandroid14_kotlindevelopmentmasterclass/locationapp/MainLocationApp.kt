package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.locationapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.ui.theme.Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme

class MainLocationApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel:LocationViewModel = viewModel()
            Udemy_TheCompleteAndroid14_KotlinDevelopmentMasterclassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    App(viewModel)
                }
            }
        }
    }
}

@Composable
fun App(viewModel: LocationViewModel){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(locationUtils = locationUtils, context = context, viewModel)
}


@Composable
fun LocationDisplay(locationUtils: LocationUtils, context: Context, viewModel: LocationViewModel){
    val location = viewModel.location.value
    val address =  location?.let { 
        locationUtils.reverseGeocodeLocation(location)
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permission ->
            if (permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permission[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                // I have access to location
                locationUtils.requestLocationUpdates(viewModel = viewModel)
            }else{
                // Ask for permission
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainLocationApp,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                        ||
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                if (rationaleRequired){
                    Toast.makeText(context, "Location permission is required for this feature to work", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, "Location permission is required. Please enable it in the android settings", Toast.LENGTH_LONG).show()
                }
            }
        })
    
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        if (location != null){
            Text(text = "Address: ${location.latitude} ${location.longitude} \n $address")
        }else{
            Text(text = "Location not available")
        }
        
        Button(onClick = { 
            if (locationUtils.hasLocationPermission(context)){
                // Permission already granted update the location
                locationUtils.requestLocationUpdates(viewModel)
            }else{
                // Request location permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}