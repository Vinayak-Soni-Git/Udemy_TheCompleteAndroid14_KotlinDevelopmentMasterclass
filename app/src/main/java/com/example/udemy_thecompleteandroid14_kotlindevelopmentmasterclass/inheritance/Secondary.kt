package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.inheritance

open class Secondary : BaseClass() {
    override fun role() {
        super.role()
        println("Knight of the house of Baseclass")
    }
}