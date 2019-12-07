package com.example.android.recipeapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.R
import com.example.android.recipeapp.model.Direction

class DirectionsAdapter (private val mContext: Context,
                         private val mDirection: List<Direction>): RecyclerView.Adapter<DirectionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.direction_list, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount(): Int {
        return mDirection.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = mDirection[position]
        holder.bind(ingredient)    }

    inner class ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var directionsText : TextView
        init {
            directionsText = itemView.findViewById(R.id.direction_name)
        }
        fun bind(item:Direction)
        {
            directionsText.text = item.directions
        }
    }
}