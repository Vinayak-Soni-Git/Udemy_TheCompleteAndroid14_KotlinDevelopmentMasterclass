package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.kotlinbasics

fun main(){
    val blueRoseVase = Vase("blue", "Rose")
    val redRoseVase = blueRoseVase.copy(color = "Red")
    println(blueRoseVase)
    println(redRoseVase)
}

data class Vase(val color:String, val design:String)