package com.example.android.recipeapp.ui.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.recipeapp.LoginActivity
import com.example.android.recipeapp.MainActivity
import com.example.android.recipeapp.R
import com.example.android.recipeapp.RegisterActivity
import com.example.android.recipeapp.common.onClick
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        mAuth = FirebaseAuth.getInstance()

        registerButton.onClick { startActivity(Intent(this, RegisterActivity::class.java))}
        loginButton.onClick { startActivity(Intent(this, LoginActivity::class.java))}

    }

    private var mAuth: FirebaseAuth? = null
    public override fun onStart() {
        super.onStart()

        // if user logged in, go to main activity
        if (mAuth!!.getCurrentUser() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
