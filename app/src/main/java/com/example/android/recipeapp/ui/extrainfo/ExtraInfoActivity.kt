package com.example.android.recipeapp.ui.extrainfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.R
import com.example.android.recipeapp.model.Direction
import com.example.android.recipeapp.model.Ingredient
import com.example.android.recipeapp.ui.adapters.DirectionsAdapter
import com.example.android.recipeapp.ui.adapters.IngredientsAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_extra_info.*

class ExtraInfoActivity : AppCompatActivity() {
    private var newList : MutableList<Ingredient>? = null
    private var inAdapter: IngredientsAdapter? = null

    private var dList :MutableList<Direction>? = null
    private var dAdapter: DirectionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_info)
        //mAuth = FirebaseAuth.getInstance()
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("recipe_name") // 1
            recipe_name.text =message
            val time = bundle.getString("time") // 2
            time_text.text =time
            val dif = bundle.getString("difficulty") // 3
            dif_text.text =dif
            val image = bundle.getString("image") // 4
            Picasso.get().load(image).into(recipe_image)
            newList = ArrayList()
            dList = ArrayList()

            newList = bundle.getParcelableArrayList("ingred")
            dList = bundle.getParcelableArrayList("directions")


            var recyclerView : RecyclerView? = null
            recyclerView = findViewById(R.id.ingredient_recyclerview)
            val linearLayoutManager = LinearLayoutManager(this)

            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
            recyclerView.layoutManager =linearLayoutManager

            inAdapter = this.let { IngredientsAdapter(it,newList as ArrayList<Ingredient>) }
            recyclerView.adapter = inAdapter


            var recyclerView2 : RecyclerView? =null
            recyclerView2 = findViewById(R.id.directions_recyclerview)
            val linearLayoutManager2 = LinearLayoutManager(this)


            recyclerView2.layoutManager =linearLayoutManager2
            dAdapter = this.let { DirectionsAdapter(it,dList as ArrayList<Direction>) }
            recyclerView2.adapter = dAdapter


        }

    }
}