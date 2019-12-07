package com.example.android.recipeapp.model


data class Recipe(val id: String?,
                  val authorName: String,
                  val authorId: String,
                  val text: String,
                  val time:String,
                  val difficulty: String,
                  val image: String,
                  val ingredients: List<Ingredient> = listOf(),
                  val directions: List<Direction> = listOf(),
                  var isFavorite: Boolean = false){ constructor() :  this ("","","","","","","",
    emptyList(), emptyList(),false)
}