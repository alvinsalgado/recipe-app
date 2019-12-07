package com.example.android.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_login.email as email1

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }

        fun loginButtonClicked(view: View) {
            if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return
            }

            if (TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return
            }

            //authenticate user
            mAuth!!.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    // If sign in fails, display a message to the user.
                    if (task.isSuccessful) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        if (password.text.toString().length < 6) {
                            password.error = getString(R.string.password_error)
                        } else {
                            Toast.makeText(this@LoginActivity, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }
