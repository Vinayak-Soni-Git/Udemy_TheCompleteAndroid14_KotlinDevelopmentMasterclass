package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.musicapp

import androidx.annotation.DrawableRes
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R

data class Lib(@DrawableRes val icon:Int, val name:String)

val libraries = listOf(Lib(R.drawable.baseline_playlist_play_24, "Playlist"),
    Lib(R.drawable.baseline_person_4_24, "Artist"),
    Lib(R.drawable.baseline_album_24, "Album"),
    Lib(R.drawable.baseline_music_note_24, "Songs"),
    Lib(R.drawable.baseline_category_24, "Genre"))