package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.inheritance

class OffSpring: Secondary(), Archery, Singer {
    override fun archery() {
        super.archery()
        println("Archery skills enhanced by offspring")
    }

    override fun sing() {
        super.sing()
        println("Singing skills enhanced by offspring")
    }
}