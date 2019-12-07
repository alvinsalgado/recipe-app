package com.example.android.recipeapp.ui.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapp.MainActivity
import com.example.android.recipeapp.R
import com.example.android.recipeapp.common.onClick
import com.example.android.recipeapp.model.Direction
import com.example.android.recipeapp.model.Ingredient
import com.example.android.recipeapp.model.Recipe
import com.example.android.recipeapp.ui.adapters.DirectionsAdapter
import com.example.android.recipeapp.ui.adapters.IngredientsAdapter
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_recipe.*
private const val KEY_RECIPE = "recipe"

class AddRecipeActivity : AppCompatActivity() {

    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageRecipePicRef: StorageReference? = null


    private var ingredientList: MutableList<Ingredient>? = null
    private var ingredientsAdapter: IngredientsAdapter? = null

    private var directionsList: MutableList<Direction>? = null
    private var directoinsAdapter: DirectionsAdapter? = null


    private var mAuth: FirebaseAuth? = null

    private var mFirebaseDatabaseInstance: FirebaseDatabase? = null
    private var mFirebaseDatabase: DatabaseReference? = null

    private var newIngredient: String? = null
    private var newDirection: String? = null


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        storageRecipePicRef = FirebaseStorage.getInstance().reference.child("Recipes Pictures")

        //Get Firebase instances
        mAuth = FirebaseAuth.getInstance()
        mFirebaseDatabaseInstance = FirebaseDatabase.getInstance()

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Add Recipe"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        // Button used to upload recipe to database
        addRecipe.onClick {
            uploadRecipe()
            Toast.makeText(applicationContext, "Successfully added recipe", Toast.LENGTH_SHORT).show()
        }
        // activity to crop images
        CropImage.activity()
            .setAspectRatio(2,2)
            .start(this@AddRecipeActivity)

        var recyclerView : RecyclerView? = null
        recyclerView = findViewById(R.id.ingredientsList)
        val linearLayoutManager = LinearLayoutManager(this)

        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager =linearLayoutManager

        ingredientList = ArrayList()
        ingredientsAdapter = this.let { IngredientsAdapter(it,ingredientList as ArrayList<Ingredient>) }
        recyclerView.adapter = ingredientsAdapter

        // button to add ingredients
        ingredient_btn.onClick {
            newIngredient = ingredient_text.text.toString()
            if(!newIngredient!!.isEmpty())
            {
                val myIngredient = Ingredient(newIngredient!!)
                ingredient_text.setText("")
                (ingredientList as ArrayList<Ingredient>).add(myIngredient)
                ingredientsAdapter?.notifyDataSetChanged()
            }else
            {
                Toast.makeText(this,"Issue adding ingredient", Toast.LENGTH_LONG).show()

            }

        }

        var recyclerView2 : RecyclerView? = null
        recyclerView2 = findViewById(R.id.directions_list)
        val linearLayoutManager2 = LinearLayoutManager(this)

        linearLayoutManager2.reverseLayout = true
        linearLayoutManager2.stackFromEnd = true
        recyclerView2.layoutManager = linearLayoutManager2
        directionsList = ArrayList()
        directoinsAdapter = this.let { DirectionsAdapter(it,directionsList as ArrayList<Direction>) }
        recyclerView2.adapter = directoinsAdapter

        // button to add directions
        direction_btn.onClick {
            newDirection = direction_text.text.toString()
            if (!newDirection!!.isEmpty()) {

                val myDirection = Direction(newDirection!!)
                direction_text.setText("")
                (directionsList as ArrayList<Direction>).add(myDirection)

                directoinsAdapter?.notifyDataSetChanged()
            }
        }

    }

    /**
     *  Retrieving image uri
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_post.setImageURI(imageUri)
        }
    }

    /**
     * Back button to previous activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * upload recipe to firebase database
     */
    fun uploadRecipe(){

        val fileRef = storageRecipePicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

        var uploadTask: StorageTask<*>
        uploadTask = imageUri?.let { fileRef.putFile(it) }!!


        uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
            if (!task.isSuccessful)
            {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation fileRef.downloadUrl
        }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
            if (task.isSuccessful)
            {

                //val ref = FirebaseDatabase.getInstance().reference.child(KEY_RECIPE)
                mFirebaseDatabase = mFirebaseDatabaseInstance!!.getReference(KEY_RECIPE)

                val downloadUrl = task.result
                myUrl = downloadUrl.toString()
                val name = mAuth!!.currentUser?.displayName ?: ""
                val id = mAuth!!.currentUser?.uid ?: ""
                val recipe =
                    ingredientList?.let { directionsList?.let { it1 ->
                        Recipe("",name,id,recipeTitle.text.toString(),time.text.toString(),difficulty.text.toString(),myUrl, it,
                            it1
                        )
                    } }
                val newRecipeReference = mFirebaseDatabaseInstance?.reference?.child(KEY_RECIPE)?.push()
                val newRecipe = recipe?.copy(id = newRecipeReference?.key)
                newRecipeReference?.setValue(newRecipe)


                Toast.makeText(this, "Recipe Information has been updated successfully.", Toast.LENGTH_LONG).show()

                val intent = Intent(this@AddRecipeActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
            }
        } )
    }
}