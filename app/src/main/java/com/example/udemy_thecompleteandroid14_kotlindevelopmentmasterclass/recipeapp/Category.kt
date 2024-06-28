package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.recipeapp

data class Category(
    val idCategory: String,
    val strCategory:String,
    val strCategoryThumb:String,
    val strCategoryDescription:String
)


data class CategoriesResponse(
    val categories:List<Category>
)