package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    
    private var mCurrentPosition:Int = 1
    private var mQuestionsList:ArrayList<Question>? = null
    private var mSelectedOptionPosition:Int = 0
    private var mUserName:String? = null
    
    private var mCorrectAnswers:Int = 0
    private var progressBar:ProgressBar? = null
    private var progressTextView:TextView? = null
    private var questionTextView:TextView? = null
    private var questionImageView:ImageView? = null

    private var optionOneTextView:TextView? = null
    private var optionTwoTextView:TextView? = null
    private var optionThreeTextView:TextView? = null
    private var optionFourTextView:TextView? = null
    
    private var buttonSubmit:Button? = null
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)
        
        mUserName = intent.getStringExtra(Constants.USER_NAME)
        
        progressBar = findViewById(R.id.progressBar)
        progressTextView = findViewById(R.id.progressTextView)
        questionTextView = findViewById(R.id.questionTextView)
        questionImageView = findViewById(R.id.questionImageView)
        
        optionOneTextView = findViewById(R.id.optionOneTextView)
        optionTwoTextView = findViewById(R.id.optionTwoTextView)
        optionThreeTextView = findViewById(R.id.optionThreeTextView)
        optionFourTextView = findViewById(R.id.optionFourTextView)
        
        buttonSubmit = findViewById(R.id.submitButton)
        
        mQuestionsList = Constants.getQuestions()
        
        setQuestion()
        
        
    }

    private fun setQuestion() {
        defaultOptionsView()
        
        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        questionImageView?.setImageResource(question.image)
        progressBar?.progress = mCurrentPosition
        progressTextView?.text = "$mCurrentPosition/${progressBar?.max}"
        questionTextView?.text = question.question

        optionOneTextView?.text = question.optionOne
        optionTwoTextView?.text = question.optionTwo
        optionThreeTextView?.text = question.optionThree
        optionFourTextView?.text = question.optionFour

        optionOneTextView?.setOnClickListener(this)
        optionTwoTextView?.setOnClickListener(this)
        optionThreeTextView?.setOnClickListener(this)
        optionFourTextView?.setOnClickListener(this)
        
        buttonSubmit?.setOnClickListener(this)
        
        
        if (mCurrentPosition == mQuestionsList!!.size){
            buttonSubmit?.text = "Finish"
        }else{
            buttonSubmit?.text = "Submit"
        }
        
    }
    
    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        optionOneTextView?.let { 
            options.add(0, it)
        }
        optionTwoTextView?.let {
            options.add(1, it)
        }
        optionThreeTextView?.let {
            options.add(2, it)
        }
        optionFourTextView?.let {
            options.add(3, it)
        }
        
        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }
    
    private fun selectedOptionView(textView: TextView, selectedOptionNum:Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.optionOneTextView->{
                optionOneTextView?.let { 
                    selectedOptionView(it, 1)
                }
            }
            R.id.optionTwoTextView->{
                optionTwoTextView?.let {
                    selectedOptionView(it, 2)
                }
            }
            R.id.optionThreeTextView->{
                optionThreeTextView?.let {
                    selectedOptionView(it, 3)
                }
            }
            R.id.optionFourTextView->{
                optionFourTextView?.let {
                    selectedOptionView(it, 4)
                }
            }
            R.id.submitButton->{
                if (mSelectedOptionPosition == 0){
                    mCurrentPosition++
                    
                    when{
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }else ->{
                            Toast.makeText(this, "You made it to the end", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWER, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer !=  mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    
                    if (mCurrentPosition == mQuestionsList!!.size){
                        buttonSubmit?.text = "Finish"
                    }else{
                        buttonSubmit?.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }
    private fun answerView(answer:Int, drawableView:Int){
        when(answer){
            1->{
                optionOneTextView?.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2->
                {
                optionTwoTextView?.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3->{
                optionThreeTextView?.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4->{
                optionFourTextView?.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }
}