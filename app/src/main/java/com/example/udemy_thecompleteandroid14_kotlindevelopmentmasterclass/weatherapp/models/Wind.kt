package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.weatherapp.models

import java.io.Serializable

data class Wind (
    val speed:Double,
    val deg:Int
):Serializable