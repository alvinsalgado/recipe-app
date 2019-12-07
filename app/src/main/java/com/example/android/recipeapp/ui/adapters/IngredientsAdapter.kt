package com.example.android.recipeapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.R
import com.example.android.recipeapp.model.Ingredient

class IngredientsAdapter(private val mContext: Context,
                         private val mIngredients: List<Ingredient>): RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: IngredientsAdapter.ViewHolder, position: Int) {

        val ingredient = mIngredients[position]
        holder.bind(ingredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mIngredients.size
    }

    inner class ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var ingredientName: TextView
        init {
            ingredientName = itemView.findViewById(R.id.ingredient_name)
        }

        fun bind(item: Ingredient)
        {
            ingredientName.text = item.ingredient
        }
    }
}