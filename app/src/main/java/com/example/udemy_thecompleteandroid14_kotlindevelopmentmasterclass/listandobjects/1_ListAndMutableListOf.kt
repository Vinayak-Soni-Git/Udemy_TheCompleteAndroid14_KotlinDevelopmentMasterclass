package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.listandobjects

fun main(){
    
    // Immutable list
    val shoppingList = listOf("Processor", "RAM", "Graphics Card", "SSD")
    
    // Mutable list
    val shoppingList2 = mutableListOf("Processor", "RAM", "Graphics Card RTX 3060", "SSD")
    shoppingList2.add("Cooling System")
    shoppingList2.remove("Graphics Card RTX 3060")
    shoppingList2.add("Graphics Card RTX 4090")
    
    println(shoppingList2)
}