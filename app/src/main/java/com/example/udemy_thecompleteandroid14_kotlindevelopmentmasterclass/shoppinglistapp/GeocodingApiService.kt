package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    
    @GET("maps/api/geocode/json")
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng:String,
        @Query("key") apiKey:String
    ):GeocodingResponse
}