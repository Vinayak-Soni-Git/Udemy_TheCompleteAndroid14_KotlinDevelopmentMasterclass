package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.kotlinbasics

fun main(){
    val numbers = listOf(1, 2 ,3)
    val doubled = numbers.map { it*2 }
    println(doubled)
}