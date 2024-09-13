package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.weatherapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        
        CallAPILogInAsyncTask("denis", "123456").execute()
    }
    
    @SuppressLint("StaticFieldLeak")
    private inner class CallAPILogInAsyncTask(val username:String, val password:String):AsyncTask<Any, Void, String>(){
        
        private lateinit var customProgressDialog:Dialog

        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            
            showProgressDialog()
        }
        
        override fun doInBackground(vararg p0: Any?): String {
            var result:String
            var connection:HttpURLConnection? = null
            try{
                val url = URL("https://www.mocky.io/v2/")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true
                
                connection.instanceFollowRedirects = false
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Accept", "application/json")
                
                connection.useCaches = false
                
                val writeDataOutputStream = DataOutputStream(connection.outputStream)
                val jsonRequest = JSONObject()
                jsonRequest.put("username", username)
                jsonRequest.put("password", password)
                
                writeDataOutputStream.writeBytes(jsonRequest.toString())
                writeDataOutputStream.flush()
                writeDataOutputStream.close()
                
                val httpResult:Int = connection.responseCode
                if(httpResult == HttpURLConnection.HTTP_OK){
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line:String?
                    try{
                        while(reader.readLine().also { line = it } != null){
                            stringBuilder.append(line + "\n")
                        }
                    }catch (e:IOException){
                        e.printStackTrace()
                    }finally {
                        try{
                            inputStream.close()
                        }catch (e:IOException){
                            e.printStackTrace()
                        }
                    }
                    result = stringBuilder.toString()
                }else{
                    result = connection.responseMessage
                }
            }catch (e:SocketTimeoutException){
                result = "Connection Timeout"
            }catch (e:Exception){
                result = "Error: " +e.message
            }finally {
                connection?.disconnect()
            }
            return result
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            
            cancelProgressDialog()
            Log.i("JSON RESPONSE RESULT: ", result!!)
            
            val responseData = Gson().fromJson(result, ResponseData::class.java)
            Log.i("Message", responseData.message)
            Log.i("User Id", "${responseData.userId}")
            Log.i("Name", responseData.name)
            Log.i("Email", responseData.email)
            Log.i("Mobile", "${responseData.mobile}")
            
            Log.i("Is Profile Completed", "${responseData.profileDetails.isProfileCompleted}")
            Log.i("Rating", "${responseData.profileDetails.rating}")
            
            
            
            val jsonObject = JSONObject(result)
            val message = jsonObject.optString("message")
            Log.i("Message", message)
            
            val userId = jsonObject.optString("user_id")
            Log.i("User Id", userId)
            
            val name = jsonObject.optString("name")
            Log.i("Name", name)
            
            val profileDetailsObject = jsonObject.optJSONObject("profile_details")
            val isProfileCompleted = profileDetailsObject.optBoolean("is_profile_completed")
            Log.i("Is Profile Completed", "$isProfileCompleted")
            
            val dataListArray = jsonObject.optJSONArray("data_list")
            Log.i("Data List Size", "${dataListArray.length()}")
            
            for(item in 0 until dataListArray.length()){
                Log.i("Value $item", "${dataListArray[item]}")
                
                val dataItemObject:JSONObject = dataListArray[item] as JSONObject
                
                val id = dataItemObject.optInt("id")
                Log.i("ID", "$id")
                val value = dataItemObject.optString("value")
                Log.i("Value", value)
            }
            
        }
        
        private fun showProgressDialog(){
            customProgressDialog = Dialog(this@DemoActivity)
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)
            customProgressDialog.show()
        }
        
        private fun cancelProgressDialog(){
            customProgressDialog.dismiss()
        }

    }
}