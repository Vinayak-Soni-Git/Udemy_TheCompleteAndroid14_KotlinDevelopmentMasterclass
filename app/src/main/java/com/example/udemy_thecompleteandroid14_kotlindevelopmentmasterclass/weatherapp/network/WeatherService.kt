package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.weatherapp.network

import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("2.5/weather")
    fun getWeather(@Query("lat") lat:Double,
                   @Query("lon") lon:Double,
                   @Query("units") units:String?,
                   @Query("appid") appId:String?
    ):Call<WeatherResponse>
}