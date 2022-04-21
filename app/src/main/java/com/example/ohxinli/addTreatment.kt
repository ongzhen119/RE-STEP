package com.example.ohxinli

import android.content.ContentValues.TAG
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
import com.example.ohxinli.databinding.ActivityAddTreatmentBinding
import com.google.firebase.firestore.FirebaseFirestore

class addTreatment : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityAddTreatmentBinding
    // creating constant keys for shared preferences.
    val SHARED_PREFS = "shared_prefs"

    var regisno: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityAddTreatmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        setContentView(binding.root)

        regisno = preferences.getString("regisno_key", null)

        binding.savebtn.setOnClickListener {
            var treatment_id: String = ""
            val description = binding.description.text.toString()
            val plan_name = binding.planname.text.toString()
            val treatment_session = binding.sessionVal.text.toString()
            val price = binding.price.text.toString()
            val regis_no = regisno.toString()

            val treatment = Treatment(
                description,
                plan_name,
                treatment_session.toInt(),
                price.toInt(),
                regis_no,
                treatment_id
            )

            if(description.isEmpty() || plan_name.isEmpty() || treatment_session.isEmpty() || price.isEmpty()){
                Toast.makeText(applicationContext, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            }else {
                db.collection("Treatment").add(treatment)
                    .addOnSuccessListener { doc ->
                        treatment_id = doc.id //get the auto generated id
                        Log.d(TAG, "DocumentSnapshot written with ID: ${doc.id}")
                        db.collection("Treatment").document(treatment_id.toString())
                            .update(
                                mapOf(
                                    "treatment_id" to treatment_id
                                )
                            )

                        Toast.makeText(
                            applicationContext,
                            "Treatment Plan Created",
                            Toast.LENGTH_SHORT
                        ).show()

                        intent = Intent(this, rehabmain::class.java)
                        startActivity(intent)
                    }
            }

        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
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
                R.id.nav_appointment -> {
                    intent = Intent(this, booking_request:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_schedule -> {
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