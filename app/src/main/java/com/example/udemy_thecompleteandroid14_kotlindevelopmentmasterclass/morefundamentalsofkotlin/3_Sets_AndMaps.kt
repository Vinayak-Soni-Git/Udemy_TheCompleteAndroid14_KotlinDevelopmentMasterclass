package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.morefundamentalsofkotlin

fun main(){
    val fruits = setOf("Orange", "Apple", "Mango", "Grape", "Apple", "Orange")
    println(fruits.size)
    
    val newFruits = fruits.toMutableList()
    newFruits.add("Water Melon")
    newFruits.add("Pear")
    println(newFruits.elementAt(4))
    
    val daysOfTheWeek = mapOf(1 to "Monday", 2 to "Tuesday", 3 to "Wednesday")
    println(daysOfTheWeek[2])
    
    for(key in daysOfTheWeek.keys){
        println("$key is to ${daysOfTheWeek[key]}")
    }
}

