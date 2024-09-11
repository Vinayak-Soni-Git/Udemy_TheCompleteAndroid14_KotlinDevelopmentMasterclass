package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityExerciseBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.DialogCustomBackConfirmationBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration:Long = 10
    
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration:Long = 30
    
    
    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    
    private var textToSpeech:TextToSpeech? = null
    private var player:MediaPlayer? = null
    
    private var exerciseStatusAdapter:ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        
        exerciseList = Constants.defaultExerciseList()
            
        textToSpeech = TextToSpeech(this, this)
        
        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
           customDialog.dismiss() 
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.exerciseStatusRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.exerciseStatusRecyclerView?.adapter = exerciseStatusAdapter
    }

    private fun setupRestView(){
        
        try{
            val soundUri = Uri.parse(
                "android.resource://com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass/"+
                        R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            
        }
        
        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.titleTextView?.visibility = View.VISIBLE
        binding?.exerciseNameTextView?.visibility = View.INVISIBLE
        binding?.flExerciseProgressBar?.visibility = View.INVISIBLE
        binding?.exerciseImageView?.visibility = View.INVISIBLE
        binding?.upcomingExerciseLabelTextView?.visibility = View.VISIBLE
        binding?.upcomingExerciseNameTextView?.visibility = View.VISIBLE
        
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        
        
        binding?.upcomingExerciseNameTextView?.text = exerciseList!![currentExercisePosition+1].getName()
        

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.titleTextView?.visibility = View.INVISIBLE
        binding?.exerciseNameTextView?.visibility = View.VISIBLE
        binding?.flExerciseProgressBar?.visibility = View.VISIBLE
        binding?.exerciseImageView?.visibility = View.VISIBLE

        binding?.upcomingExerciseLabelTextView?.visibility = View.INVISIBLE
        binding?.upcomingExerciseNameTextView?.visibility = View.INVISIBLE

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        
        binding?.exerciseImageView?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.exerciseNameTextView?.text = exerciseList!![currentExercisePosition].getName()
        
        
        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {

        binding?.countdownProgressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.countdownProgressBar?.progress = 10 - restProgress
                binding?.timerTextView?.text =
                    (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseStatusAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {

        binding?.exerciseCountdownProgressBar?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.exerciseCountdownProgressBar?.progress = 30 - exerciseProgress
                binding?.exerciseTimerTextView?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {

                
                
                if (currentExercisePosition < exerciseList?.size!! - 1) {

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseStatusAdapter!!.notifyDataSetChanged()
                    
                    setupRestView()
                } else {

                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()

    }

    override fun onInit(status: Int) {
        if(status ==TextToSpeech.SUCCESS){
            val result = textToSpeech?.setLanguage(Locale.US)
            
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "The language specified is not supported", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun speakOut(text:String){
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onBackPressed() {
        customDialogForBackButton()
//        super.onBackPressed()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(textToSpeech != null){
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        if(player != null){
            player!!.stop()
        }
        binding = null
    }
}