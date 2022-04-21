package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ohxinli.databinding.ActivityEditUserBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditUser : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityEditUserBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var icno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        icno = preferences.getString("icno_key", null)

        //display user information
        displayUserInfo()

        binding.savebtn.setOnClickListener{
            updateUserInfo()
            displayUserInfo()
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    fun displayUserInfo(){
        db = FirebaseFirestore.getInstance()

        db.collection("User").whereEqualTo("ic_no", icno).get()
            .addOnSuccessListener { doc ->
                for(document in doc){
                    binding.username.setText(document.get("username").toString())
                    binding.name.setText(document.get("name").toString())
                    binding.phone.setText(document.get("phone_no").toString())
                    binding.email.setText(document.get("user_email").toString())
                    binding.ic.setText(document.get("ic_no").toString())
                    binding.yrs.setText(document.get("drug_duration").toString())
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error Loading File: ${it.toString()}")
            }
    }

    fun updateUserInfo() {

        val username = binding.username.text.toString()
        val name = binding.name.text.toString()
        val phone_no = binding.phone.text.toString()
        val drug_duration = binding.yrs.text.toString()
        val user_email = binding.email.text.toString()
        val ic_no = binding.ic.text.toString()

        db.collection("User").document(ic_no)
            .update(
                mapOf(
                    "username" to username,
                    "name" to name,
                    "phone_no" to phone_no,
                    "drug_duration" to drug_duration,
                    "user_email" to user_email
                )
            )
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "User Profile Updated", Toast.LENGTH_SHORT).show()
            }
    }

    fun setActionBar(drawer: DrawerLayout){

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.abs_layout)
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
//        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.menu_icon)
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        val menu: ImageButton = findViewById(R.id.menu_icon)

        menu.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)
        }

        val navView = binding.navView
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    intent = Intent(this, usermain:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    intent = Intent(this, EditUser:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_myBookings -> {
                    intent = Intent(this, myBooking:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    intent = Intent(this, login:: class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}