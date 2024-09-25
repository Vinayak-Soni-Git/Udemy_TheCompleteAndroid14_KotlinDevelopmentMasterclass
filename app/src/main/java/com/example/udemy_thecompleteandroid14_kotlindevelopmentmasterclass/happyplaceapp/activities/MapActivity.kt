package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityMapBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.models.HappyPlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding:ActivityMapBinding
    private var mHappyPlaceDetails:HappyPlaceModel? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_map)
        if(intent.hasExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS)){
            mHappyPlaceDetails = intent.getParcelableExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS)
        }
        
        if(mHappyPlaceDetails != null){
            setSupportActionBar(binding.toolbarMap)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mHappyPlaceDetails!!.title
            binding.toolbarMap.setNavigationOnClickListener{
                onBackPressed()
            }
        }
        val supportMapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(
            mHappyPlaceDetails!!.latitude,
            mHappyPlaceDetails!!.longitude
        )
        googleMap.addMarker(MarkerOptions().position(position).title(mHappyPlaceDetails!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)
    }
}