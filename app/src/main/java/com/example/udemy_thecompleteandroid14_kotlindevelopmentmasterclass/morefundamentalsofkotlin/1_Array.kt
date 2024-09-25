package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.morefundamentalsofkotlin

fun main(){
    val numbers:IntArray = intArrayOf(1, 2, 3, 4, 5, 6)
//    val numbers2 = intArrayOf(1, 2, 3, 4, 5, 6)
//    val numbers3 = arrayOf(1, 2, 3, 4, 5, 6)
    
    print(numbers.contentToString())
    
    for(element in numbers){
        print(element)
    }
    print(numbers[0])
    print(numbers[3])
    
    numbers[0] = 0
    numbers[1] = 1
    print(numbers[0])
    print(numbers[1])
    
    val fruits = arrayOf(Fruit("Apple", 2.5), Fruit("Grape", 4.5))
    for(index in fruits.indices){
        print("${fruits[index].name} is in index $index")
    }
    
}

data class Fruit(val name:String, val price:Double)