package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.functionsobjectsandcoffeemachines

class Dog (val name:String, val breed:String){
    
    
    init {
        bark(name)
    }
    fun bark(name:String){
        println("$name says Woof Woof")
    }
}

fun main(){
    val daisy = Dog("Daisy", "Poodle")
}