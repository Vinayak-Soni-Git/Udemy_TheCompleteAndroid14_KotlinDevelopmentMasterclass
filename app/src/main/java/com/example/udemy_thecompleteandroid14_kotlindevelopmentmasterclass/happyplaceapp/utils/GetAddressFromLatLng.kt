package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import java.util.Locale

class GetAddressFromLatLng(
    context:Context, 
    private val latitude:Double, 
    private val longitude:Double):AsyncTask<Void, String, String>() {
        
    override fun doInBackground(vararg p0: Void?): String {
        try{
            val addressList:List<Address>? = geoCoder.getFromLocation(latitude, longitude, 1)
            if(addressList != null && addressList.isNotEmpty()){
                val address:Address = addressList[0]
                val stringBuilder = StringBuilder()
                for(i in 0..address.maxAddressLineIndex){
                    stringBuilder.append(address.getAddressLine(i)).append(" ")
                }
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
                return stringBuilder.toString()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ""
    }
    
    private val geoCoder:Geocoder = Geocoder(context, Locale.getDefault())
    private lateinit var mAddressListener:AddressListener

    override fun onPostExecute(result: String?) {
        if(result == null){
            mAddressListener.onError()
        }else{
            mAddressListener.onAddressFound(result)
        }
        super.onPostExecute(result)
    }
    
    fun setAddressListener(addressListener:AddressListener){
        mAddressListener = addressListener
    }
    
    fun getAddress(){
        execute()
    }
    
    interface AddressListener{
        fun onAddressFound(address:String)
        fun onError()
    }

}