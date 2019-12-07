package com.example.android.recipeapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.recipeapp.R
import com.example.android.recipeapp.common.onClick
import com.example.android.recipeapp.model.UserResponse
import com.example.android.recipeapp.ui.edit.EditActivity
import com.example.android.recipeapp.ui.start.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var userId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile,container,false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton.onClick { FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, StartActivity::class.java))
            activity?.finish() }
        editButton.onClick {
            startActivity(Intent(activity,EditActivity::class.java))

        }

        mFirebaseInstance = FirebaseDatabase.getInstance()

        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")

        val user = FirebaseAuth.getInstance().getCurrentUser()

        userId = user?.getUid()

        addUserChangeListener()
    }


    /**
     * User data change listener to display data on view
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

                Log.e(TAG, "User data is changed!" + user.username + ", " + user.email)
                Picasso.get().load(user.image).placeholder(R.drawable.profile).into(imageView)

                txt_user.text = getString(R.string.username_text,user.username)
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
}