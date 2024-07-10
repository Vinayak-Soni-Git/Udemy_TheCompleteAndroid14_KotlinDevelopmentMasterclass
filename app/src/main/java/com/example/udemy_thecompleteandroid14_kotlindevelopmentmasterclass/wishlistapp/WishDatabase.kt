package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.wishlistapp

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Wish::class], version = 1, exportSchema = false)
abstract class WishDatabase:RoomDatabase() {
    abstract fun wishDao():WishDao
}