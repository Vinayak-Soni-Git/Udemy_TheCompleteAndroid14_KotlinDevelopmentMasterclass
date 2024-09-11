package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    
    @ColumnInfo(name = "name")
    val name:String = "",
    
    @ColumnInfo(name = "emailId")
    val email:String = ""
)