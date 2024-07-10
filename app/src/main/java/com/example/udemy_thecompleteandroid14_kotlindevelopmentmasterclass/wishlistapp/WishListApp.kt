package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.wishlistapp

import android.app.Application

class WishListApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}