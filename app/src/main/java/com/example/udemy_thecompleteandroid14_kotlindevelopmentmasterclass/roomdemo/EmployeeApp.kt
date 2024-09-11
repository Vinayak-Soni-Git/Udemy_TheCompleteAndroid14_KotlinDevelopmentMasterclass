package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.roomdemo

import android.app.Application

class EmployeeApp:Application() {
    val db by lazy{
        EmployeeDatabase.getInstance(this)
    }
}
