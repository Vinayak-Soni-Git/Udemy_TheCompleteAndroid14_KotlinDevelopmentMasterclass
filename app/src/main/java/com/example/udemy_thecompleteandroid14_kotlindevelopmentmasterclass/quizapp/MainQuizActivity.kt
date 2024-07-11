package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R

class MainQuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_quiz)
        
        val nameEditText:AppCompatEditText = findViewById(R.id.nameEditText)
        val buttonStart:Button = findViewById(R.id.buttonStart)
        
        buttonStart.setOnClickListener{
            if (nameEditText.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(this, QuizQuestionsActivity::class.java)
                intent.putExtra(Constants.USER_NAME, nameEditText.text.toString())
                startActivity(intent)
            }
        }
    }
}