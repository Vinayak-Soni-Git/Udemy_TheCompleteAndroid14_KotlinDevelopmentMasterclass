package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.recipeapp

sealed class Screen(val route:String) {
    object RecipeScreen:Screen("recipescreen")
    object DetailScreen:Screen("detailscreen")
}