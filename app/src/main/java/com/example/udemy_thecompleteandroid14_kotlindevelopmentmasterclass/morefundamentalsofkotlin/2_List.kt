package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.morefundamentalsofkotlin

fun main(){
    val months = listOf("January", "February", "March", "April")
    val types = listOf(1, 2, true, false, "string", 4.5f)
    println(months.size)
    println(types.size)
    
    val additionalMonths = months.toMutableList()
    val newMonths = listOf("May", "June", "July", "August")
    additionalMonths.addAll(newMonths)
    println(additionalMonths)
}