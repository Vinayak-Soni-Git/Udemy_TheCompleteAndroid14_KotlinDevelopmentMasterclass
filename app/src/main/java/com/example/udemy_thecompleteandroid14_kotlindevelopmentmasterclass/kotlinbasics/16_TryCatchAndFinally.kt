package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.kotlinbasics

fun main(){
    println("Please enter a number")
    try {
        val number = readln().toInt()
        println("User entered $number")
    } catch (e:Exception){
        println(e.message)
    } finally {
        println("This will be execute regardless. Error or no error")
    }
}