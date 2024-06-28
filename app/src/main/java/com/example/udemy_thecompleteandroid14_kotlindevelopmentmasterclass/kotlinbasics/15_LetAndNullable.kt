package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.kotlinbasics

fun main(){
    // nullable string
    val name:String? = "Ella"
    
    name?.let { 
        println(it.toUpperCase())
    }
}