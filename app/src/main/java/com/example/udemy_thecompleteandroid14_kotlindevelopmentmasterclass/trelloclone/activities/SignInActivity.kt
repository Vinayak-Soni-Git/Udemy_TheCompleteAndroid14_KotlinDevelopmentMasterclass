package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivitySignInBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {
    
    private lateinit var binding:ActivitySignInBinding
    private lateinit var auth:FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()
        
        binding.btnSignIn.setOnClickListener{
            signInRegisteredUser()
        }
        
        setupActionBar()
    }
    fun signInSuccess(user:User){
        hideProgressDialog()
        startActivity(Intent(this, MainTrelloActivity::class.java))
        finish()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarSignInActivity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        binding.toolbarSignInActivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private fun signInRegisteredUser(){
        val email:String = binding.etEmail.text.toString().trim{it<=' '}
        val password:String = binding.etPassword.text.toString().trim{it<=' '}
        
        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        startActivity(Intent(this, MainTrelloActivity::class.java))
                    }else{
                        Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(email:String, password:String):Boolean{
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }
    }
}