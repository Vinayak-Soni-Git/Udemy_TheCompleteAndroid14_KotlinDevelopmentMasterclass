package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlin.math.log

class LocationViewModel:ViewModel() {
    private val _location = mutableStateOf<ShoppingLocationData?>(null)
    val location:State<ShoppingLocationData?> = _location
    
    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address:State<List<GeocodingResult>> = _address
    
    fun updateLocation(newLocation:ShoppingLocationData){
        _location.value = newLocation
    }
    
    fun fetchAddress(latLng: String){
        try {
            viewModelScope.launch { 
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latLng, ""
                )
                _address.value = result.results
            }
        }catch (e:Exception){
            Log.d("res1", "${e.cause} ${e.message}")
        }
    }
}