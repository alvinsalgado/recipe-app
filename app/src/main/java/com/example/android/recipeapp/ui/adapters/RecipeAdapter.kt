package com.example.android.recipeapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.R
import com.example.android.recipeapp.model.Recipe
import com.example.android.recipeapp.model.UserResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class RecipeAdapter (private val mContext: Context,
                     private val mPost: List<Recipe>,
                     private val clickListener: (Recipe) -> Unit,
                     private val onFavoriteClickHandler: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeHolder>()
{

    private var firebaseUser : FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list, parent, false)
        return RecipeHolder(view, clickListener,onFavoriteClickHandler)
    }

    override fun getItemCount(): Int {
        return mPost.size    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        val recipe = mPost[position]

        holder.bind(recipe)
        Picasso.get().load(recipe.image).into(holder.recipeImage)

        publisherInfo(holder.profileImage,holder.recipeMaker, recipe.authorId)
    }

    private fun publisherInfo(profileImage: ImageView, recipeMaker: TextView, authorId: String) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("users").child(authorId)
        usersRef.addValueEventListener(object: ValueEventListener

        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val user = p0.getValue(UserResponse::class.java)
                    Picasso.get().load(user!!.image).placeholder(R.drawable.profile).into(profileImage)
                    if (user != null) {
                        recipeMaker.setText(user.username)
                    }
                }
            }
        })
    }



}