package com.example.android.recipeapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.recipeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_login.email as email1

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mFirebaseDatabaseInstance: FirebaseDatabase? = null
    private var mFirebaseDatabase: DatabaseReference? = null

    private var userId: String? = null
    private var emailAddress: String? = null

    private var imageId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Get Firebase instances
        mAuth = FirebaseAuth.getInstance()
        mFirebaseDatabaseInstance = FirebaseDatabase.getInstance()

    }


    fun onRegisterClicked(view: View) {
        if (TextUtils.isEmpty(username.text.toString())) {
            Toast.makeText(applicationContext, "Enter username!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(email.text.toString())) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password.text.toString())) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.text.toString().length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
            return
        }

        //create user
        mAuth!!.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this@RegisterActivity) { task ->
                Toast.makeText(this@RegisterActivity, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()
                // If sign in fails, display a message to the user.
                if (!task.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Authentication failed." + task.exception!!,
                        Toast.LENGTH_LONG).show()
                    Log.e("MyTag", task.exception!!.toString())
                } else {
                    mAuth!!.currentUser?.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(username.text.toString())
                            .build()
                    )

                    // get reference to 'users' node
                    mFirebaseDatabase = mFirebaseDatabaseInstance!!.getReference("users")

                    val user = FirebaseAuth.getInstance().currentUser

                    val id = getUserId()
                    // add username, email to database
                    userId = user!!.uid
                    emailAddress = user.email

                    // Defalut image
                    imageId = "https://firebasestorage.googleapis.com/v0/b/skilled-index-205718.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=ae02f67e-354f-448d-ada9-ccbeaad2a4e0"
                    val myUsers = User(userId!!,username.text.toString(),emailAddress!!, imageId!!)



                    mFirebaseDatabase!!.child(userId!!).setValue(myUsers)

                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                }
            }
    }

    fun onLoginClicked(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun getUserId(): String = mAuth!!.currentUser?.uid ?: ""
}