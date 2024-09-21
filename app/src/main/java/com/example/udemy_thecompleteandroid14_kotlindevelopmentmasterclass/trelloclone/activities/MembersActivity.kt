package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities

import android.app.Activity
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityMembersBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.MemberListItemAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase.FireStoreClass
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MembersActivity : BaseActivity() {
    private lateinit var mBoardDetails:Board
    private lateinit var binding:ActivityMembersBinding
    private lateinit var mAssignedMembersList:ArrayList<User>
    private var anyChangesMade:Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAILS)!!
        }
        
        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }
    
    fun memberDetails(user: User){
        mBoardDetails.assignedTo.add(user.id)
        FireStoreClass().assignMemberToBoard(this, mBoardDetails, user)
    }
    
    private fun setupActionBar() {
        val toolBarMembersActivity: Toolbar = findViewById(R.id.toolbar_members_activity)
        setSupportActionBar(toolBarMembersActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }
        
        toolBarMembersActivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    fun setupMembersList(list:ArrayList<User>){
        mAssignedMembersList = list
        hideProgressDialog()
        binding.rvMembersList.layoutManager = LinearLayoutManager(this)
        binding.rvMembersList.setHasFixedSize(true)
        val adapter = MemberListItemAdapter(this, list)
        binding.rvMembersList.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trello_menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionAddMember->{
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun dialogSearchMember(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.trello_dialog_search_member)
        val tvAdd:TextView = dialog.findViewById(R.id.tv_add_member)
        val tvCancel:TextView = dialog.findViewById(R.id.tv_cancel_member)
        val etEmailSearchMember:EditText = dialog.findViewById(R.id.et_email_search_member)
        tvAdd.setOnClickListener{
            val email = etEmailSearchMember.text.toString()
            if(email.isNotEmpty()){
                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                FireStoreClass().getMemberDetails(this, email)
            }else{
                Toast.makeText(this@MembersActivity, "Please enter members email address.", Toast.LENGTH_SHORT).show()
            }
        }
        tvCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        if(anyChangesMade){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }
    
    fun memberAssignedSuccess(user:User){
        hideProgressDialog()
        mAssignedMembersList.add(user)
        anyChangesMade = true
        setupMembersList(mAssignedMembersList)
        
        SendNotificationToUserAsyncTask(mBoardDetails.name, user.fcmToken)
    }
    
    private inner class SendNotificationToUserAsyncTask(val boardName:String, val token:String):AsyncTask<Any, Void, String>(){


        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog(resources.getString(R.string.please_wait))
        }
        
        override fun doInBackground(vararg p0: Any?): String {
            var result:String
            var connection:HttpURLConnection? = null
            try{
                val url = URL(Constants.FCM_BASE_URL)
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true
                connection.instanceFollowRedirects = false
                connection.requestMethod = "POST"
                
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Accept", "application/json")
                
                connection.setRequestProperty(Constants.FCM_AUTHORIZATION, "${Constants.FCM_KEY}=${Constants.FCM_SERVER_KEY}")
                connection.useCaches = false
                
                val writer = DataOutputStream(connection.outputStream)
                val jsonRequest = JSONObject()
                val dataObject = JSONObject()
                dataObject.put(Constants.FCM_KEY_TITLE, "Assigned to the board $boardName")
                dataObject.put(Constants.FCM_KEY_MESSAGE, "You have been assigned to new Board by ${mAssignedMembersList[0].name}")
                jsonRequest.put(Constants.FCM_KEY_DATA, dataObject)
                jsonRequest.put(Constants.FCM_KEY_TO, token)
                writer.writeBytes(jsonRequest.toString())
                writer.flush()
                writer.close()
                
                val httpResult:Int = connection.responseCode
                if(httpResult == HttpURLConnection.HTTP_OK){
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    
                    val stringBuilder = StringBuilder()
                    var line:String?
                    try{
                        while (reader.readLine().also { line = it } != null){
                            stringBuilder.append(line+"\n")
                        }
                    }catch (e: IOException){
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
                result = "Error: "+e.message
            }finally {
                connection?.disconnect()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            hideProgressDialog()
        }

    }
}