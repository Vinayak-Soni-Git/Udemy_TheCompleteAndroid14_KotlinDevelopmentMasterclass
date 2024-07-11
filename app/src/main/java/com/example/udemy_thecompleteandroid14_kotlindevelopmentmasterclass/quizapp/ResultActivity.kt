package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        
        val nameTextView:TextView = findViewById(R.id.tv_name)
        val scoreTextView:TextView = findViewById(R.id.tv_score)
        val buttonFinish:Button = findViewById(R.id.btn_finish)
        
        nameTextView.text = intent.getStringExtra(Constants.USER_NAME)
        val totalQuestion = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWER, 0)
        
        scoreTextView.text = "Your Score is $correctAnswers out of $totalQuestion"
        buttonFinish.setOnClickListener{
            startActivity(Intent(this, MainQuizActivity::class.java))
        }
    }
}