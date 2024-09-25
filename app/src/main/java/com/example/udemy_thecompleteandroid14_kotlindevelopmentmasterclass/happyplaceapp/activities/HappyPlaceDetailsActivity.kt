package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityHappyPlaceDetailsBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.models.HappyPlaceModel

class HappyPlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHappyPlaceDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        var happyPlaceDetailModel:HappyPlaceModel ?= null
        
        if(intent.hasExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel = intent.getParcelableExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS)!!
        }
        
        if(happyPlaceDetailModel != null){
            setSupportActionBar(binding.toolbarHappyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title
            binding.toolbarHappyPlaceDetail.setNavigationOnClickListener{
                onBackPressed()
            }
            binding.ivPlaceDetailsImage.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            binding.tvDescription.text = happyPlaceDetailModel.description
            binding.tvLocation.text = happyPlaceDetailModel.location
            
            binding.btnViewOnMap.setOnClickListener{
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailModel)
                startActivity(intent)
            }
        }
    }
}