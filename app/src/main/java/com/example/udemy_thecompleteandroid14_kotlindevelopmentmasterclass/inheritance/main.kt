package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.inheritance

fun main(){
    val obj1 = BaseClass()
    obj1.coreValues()
    
    val obj2 = Secondary()
    obj2.coreValues()
    
    obj2.role()
    
    val obj3 = Tertiary()
    obj3.role()
    
    
    var frank = OffSpring()
    frank.archery()
    frank.role()
    frank.coreValues()
    frank.sing()
}