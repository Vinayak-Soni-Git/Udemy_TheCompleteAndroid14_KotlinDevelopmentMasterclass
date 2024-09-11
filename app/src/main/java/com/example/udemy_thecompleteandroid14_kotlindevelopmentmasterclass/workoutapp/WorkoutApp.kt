package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.workoutapp

import android.app.Application

class WorkoutApp:Application() {
    val db by lazy { 
        HistoryDatabase.getInstance(this)
    }
}