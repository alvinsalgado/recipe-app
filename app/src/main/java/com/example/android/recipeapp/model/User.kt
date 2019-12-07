package com.example.android.recipeapp.model

data class UserResponse(val id: String = "",
                        val username: String = "",
                        val email: String = "",
                        val image: String = "")

data class User(val id: String,
                 val username: String,
                 val email: String,
                 val image:String,
                 val favoriteRecipes: List<Recipe> = listOf())