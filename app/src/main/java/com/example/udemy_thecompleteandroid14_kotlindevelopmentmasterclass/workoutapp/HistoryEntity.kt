package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.workoutapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WorkoutHistory")
data class HistoryEntity (
    @PrimaryKey
    val date:String
)