package com.example.android.recipeapp.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.R
import com.example.android.recipeapp.common.onClick
import com.example.android.recipeapp.model.Recipe
import kotlinx.android.synthetic.main.recipe_list.view.*

class RecipeHolder (
    itemView: View,
    private inline val clickListener:(Recipe) ->Unit,
    private inline val FavoriteClickListener:(Recipe)->Unit)
    : RecyclerView.ViewHolder(itemView) {


    var recipeNames: TextView
    var recipeMaker: TextView
    var recipeImage : ImageView
    var profileImage : ImageView
    var favButton: ImageView
    init {
        recipeNames = itemView.findViewById(R.id.recipeName)
        recipeMaker = itemView.findViewById(R.id.RecipeCreator)
        recipeImage = itemView.findViewById(R.id.recipeImage)
        profileImage = itemView.findViewById(R.id.profileImage)
        favButton = itemView.findViewById(R.id.fav_btn)
    }

    fun bind(recipe: Recipe)
    {
        favButton.onClick { FavoriteClickListener(recipe) }
        recipeNames.text =recipe.text
        itemView.onClick { clickListener(recipe) }
    }
}