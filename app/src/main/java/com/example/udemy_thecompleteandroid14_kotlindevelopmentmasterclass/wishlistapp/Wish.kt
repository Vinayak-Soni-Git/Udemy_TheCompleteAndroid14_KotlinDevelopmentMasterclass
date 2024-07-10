package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.wishlistapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishes")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    
    @ColumnInfo("title")
    val title:String = "",
    
    @ColumnInfo("description")
    val description:String = ""
)

object DummyWish{
    val wishList = listOf(
        Wish(title = "Google Watch 2", description = "An android watch"),
        Wish(title = "Oculus Quest 2", description = "A VR headset for playing games"),
        Wish(title = "A Sci-fi, Book", description = "A Science fiction book from my best seller"),
        Wish(title = "Bean bag", description = "A comfy bean bag to substitute for a chair")
    )
}