package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.WHITE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import com.example.ohxinli.databinding.AbsLayoutBinding
import com.example.ohxinli.databinding.ActivityCreateUserBinding
import com.example.ohxinli.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.drawable.ColorDrawable




class create_user<override> : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityCreateUserBinding
//    private lateinit var binding2: AbsLayoutBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"

    var icno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setActionBar()
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        icno = preferences.getString("icno_key", null)

        binding.savebtn.setOnClickListener {
            val username = binding.username.text.toString()
            val name = binding.name.text.toString()
            val phone_no = binding.phone.text.toString()
            val ic_no = binding.ic.text.toString()
            val user_password = binding.password1.text.toString()
            val confirmPassword = binding.password2.text.toString()
            val drug_duration = binding.yrs.text.toString()
            val user_email = binding.email.text.toString()

            if(username.isEmpty() || name.isEmpty()|| phone_no.isEmpty() || ic_no.isEmpty() || user_email.isEmpty() || user_password.isEmpty() || drug_duration.isEmpty()){
                Toast.makeText(applicationContext, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            }else if(user_password != confirmPassword){
                Toast.makeText(applicationContext, "Confirm password is different from password", Toast.LENGTH_SHORT).show()
            }else{
                val user = User(
                    ic_no,
                    drug_duration.toInt(),
                    name,
                    phone_no,
                    user_email,
                    user_password,
                    username
                )

                db.collection("User").document("$ic_no").set(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            applicationContext,
                            "User Profile Created",
                            Toast.LENGTH_SHORT
                        ).show()

                        var editor: SharedPreferences.Editor = preferences.edit()
                        editor.putString("icno_key", ic_no)

                        editor.apply()

                        intent = Intent(this, usermain::class.java)
                        startActivity(intent)
                    }
            }

        }
    }

    fun setActionBar(){
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.abs_layout)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
    }
}