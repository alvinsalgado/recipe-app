package com.example.android.recipeapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.R
import com.example.android.recipeapp.model.*
import com.example.android.recipeapp.ui.adapters.RecipeAdapter
import com.example.android.recipeapp.ui.extrainfo.ExtraInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val KEY_RECIPE = "recipe"
class HomeFragment : Fragment() {
    private var recipeAdapter: RecipeAdapter? = null
    private var recipeList : MutableList<Recipe>? = null
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?)
    {
        var recyclerView : RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycler_view)
        val linearLayoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager = linearLayoutManager

        recipeList = ArrayList()
        recipeAdapter = context?.let { RecipeAdapter(it,recipeList as ArrayList<Recipe>,{ partItem : Recipe -> partItemClicked(partItem)},{ favItem: Recipe -> favItemClicked(favItem)}) }
        recyclerView.adapter = recipeAdapter

        retrieveRecipes()
    }

    /**
     * Retrieve recipes from database and add to list
     */
    private fun retrieveRecipes()
    {
        val recipeRef = FirebaseDatabase.getInstance().reference.child(KEY_RECIPE)

        recipeRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("Retrieving Recipes", "Failed to read recipes from database", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                recipeList?.clear()

                for (snapshot in p0.children)
                {
                    val post = snapshot.getValue(Recipe::class.java)
                    recipeList?.add(post!!)
                    recipeAdapter?.notifyDataSetChanged()
                }
            }
        }
        )
    }

    /**
     * Click listener to recipe to display extra infromation about the recipe
     */
    private fun partItemClicked(partItem: Recipe){
        Toast.makeText(context,"clicked: ${partItem.text}", Toast.LENGTH_LONG).show()
        val nextScreenIntent = Intent(context, ExtraInfoActivity::class.java)
        nextScreenIntent.putExtra("recipe_name",partItem.text.toString())
        nextScreenIntent.putExtra("image",partItem.image.toString())
        nextScreenIntent.putExtra("time",partItem.time.toString())
        nextScreenIntent.putExtra("difficulty",partItem.difficulty.toString())
        nextScreenIntent.putParcelableArrayListExtra("ingred",partItem.ingredients as ArrayList<Ingredient>)
        nextScreenIntent.putParcelableArrayListExtra("directions",partItem.directions as ArrayList<Direction>)

        startActivity(nextScreenIntent)
    }
    private fun favItemClicked(favItem:Recipe)
    {
        val user = FirebaseAuth.getInstance().currentUser
        Toast.makeText(context,"clicked: ${favItem.authorName}", Toast.LENGTH_LONG).show()
        userId = user!!.uid


    }


}