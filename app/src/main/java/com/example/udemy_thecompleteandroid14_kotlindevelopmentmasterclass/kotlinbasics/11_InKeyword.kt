package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.kotlinbasics

fun main(){
    val enteredValue = readln()
    val age = enteredValue.toInt()
    
    if (age in 18..39){
        println("You can go to the club")
    }else if(age >= 40){
        println("You are too old")
    }else{
        println("You are too young to go into the club!")
    }
}