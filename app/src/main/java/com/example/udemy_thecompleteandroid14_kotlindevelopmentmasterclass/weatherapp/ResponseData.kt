package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.weatherapp

data class ResponseData(
    val message:String,
    val userId:Int,
    val name:String,
    val email:String,
    val mobile:Long,
    val profileDetails:ProfileDetails,
    val dataList:List<DataListDetail>
)

data class ProfileDetails(
    val isProfileCompleted: Boolean,
    val rating:Double
)
data class DataListDetail(
    val id:Int,
    val value:String
)