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
import com.example.ohxinli.databinding.ActivityEditTreatmentBinding
import com.google.firebase.firestore.FirebaseFirestore

class editTreatment : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityEditTreatmentBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null
    var treatment_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityEditTreatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        regisno = preferences.getString("regisno_key", null)

        //display information
        displayInfo(regisno)

        binding.savebtn.setOnClickListener{
            updateInfo()
            displayInfo(regisno)
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    fun displayInfo(regisno: String?){
        db = FirebaseFirestore.getInstance()

        db.collection("Treatment").whereEqualTo("regis_no", regisno).get()
            .addOnSuccessListener { doc ->
                for(document in doc){
                    treatment_id = document.get("treatment_id").toString()
                    binding.description.setText(document.get("description").toString())
                    binding.planname.setText(document.get("plan_name").toString())
                    binding.sessionVal.setText(document.get("treatment_session").toString())
                    binding.price.setText(document.get("price").toString())
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error Loading File: ${it.toString()}")
            }
    }

    fun updateInfo() {

        val description = binding.description.text.toString()
        val plan_name = binding.planname.text.toString()
        val treatment_session = binding.sessionVal.text.toString()
        val price = binding.price.text.toString()
        val regis_no = regisno.toString()

        if(description.isEmpty() || plan_name.isEmpty() || treatment_session.isEmpty() || price.isEmpty()){
            Toast.makeText(applicationContext, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
        }else {
            db.collection("Treatment").document(treatment_id.toString())
                .update(
                    mapOf(
                        "description" to description,
                        "plan_name" to plan_name,
                        "treatment_session" to treatment_session,
                        "price" to price,
                        "regis_no" to regis_no,
                        "treatment_id" to treatment_id
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Treatment Updated", Toast.LENGTH_SHORT)
                        .show()
                }
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
                    intent = Intent(this, rehabmain:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_treatment -> {
                    intent = Intent(this, treatmentlist:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_appointment -> { //change
                    intent = Intent(this, booking_request:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_schedule -> { //change
                    intent = Intent(this, schedule:: class.java)
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