package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.shoppinglistapp

data class ShoppingLocationData(
    val latitude:Double,
    val longitude:Double
)

data class GeocodingResponse(
    val results:List<GeocodingResult>,
    val status:String
)

data class GeocodingResult(
    val formattedAddress:String
)