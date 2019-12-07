package com.example.android.recipeapp.ui.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.android.recipeapp.MainActivity
import com.example.android.recipeapp.R
import com.example.android.recipeapp.common.onClick
import com.example.android.recipeapp.model.UserResponse
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.fragment_profile.*

class EditActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private var checker = ""
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null

    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")

        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")

        val user = FirebaseAuth.getInstance().getCurrentUser()

        // add it only if it is not saved to database
        userId = user?.getUid()

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Edit Profile"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        change_image_text_btn.onClick {
            checker = "clicked"

            CropImage.activity()
                .setAspectRatio(1,1)
                .start(this@EditActivity)
        }

        btn_update.onClick {
            if (checker == "clicked")
            {
                uploadImageAndUpdateInfo()
            }
            else
            {
                updateUserInfoOnly()
            }
        }

        addUserChangeListener()

    }


    /**
     * Back button to previous activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Retrieve image uri
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null)
        {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image_view_profile_frag.setImageURI(imageUri)
        }
    }
    /**
     * User data change listener
     */
    private fun addUserChangeListener() {

        // User data change listener
        mFirebaseDatabase!!.child(userId!!).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(UserResponse::class.java)

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!")
                    return
                }

                // Display data to view
                Picasso.get().load(user.image).placeholder(R.drawable.profile).into(profile_image_view_profile_frag)
                email.setText(user.email)
                name.setText(user.username)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException())
            }
        })
    }

    companion object {
        private val TAG = EditActivity::class.java.getSimpleName()
    }

    /**
     * Update user information only then exit activity
     */
    private fun updateUserInfoOnly()
    {
        val name = name.getText().toString()
        val email = email.getText().toString()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
            mFirebaseDatabase!!.child(userId!!).child("username").setValue(name)
            mFirebaseDatabase!!.child(userId!!).child("email").setValue(email)
            Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@EditActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
            Toast.makeText(applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()


    }

    /**
     * Update user information and image then exit activity
     */
    private fun uploadImageAndUpdateInfo()
    {
        val name = name.getText().toString()



        val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + ".jpg")

        var uploadTask: StorageTask<*>
        uploadTask = fileRef.putFile(imageUri!!)

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

                val downloadUrl = task.result
                myUrl = downloadUrl.toString()
                mFirebaseDatabase!!.child(userId!!).child("username").setValue(name)
                mFirebaseDatabase!!.child(userId!!).child("image").setValue(myUrl)


                Toast.makeText(this, "Update successful.", Toast.LENGTH_LONG).show()

                val intent = Intent(this@EditActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
            }
        } )
    }


}
