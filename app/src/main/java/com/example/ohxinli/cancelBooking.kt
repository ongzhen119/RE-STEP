package com.example.ohxinli

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ohxinli.databinding.ActivityCancelBookingBinding
import com.example.ohxinli.databinding.ActivityEditRehabBinding
import com.example.ohxinli.databinding.ActivityViewBookingBinding
import com.google.firebase.firestore.FirebaseFirestore

class cancelBooking : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityCancelBookingBinding

    //creating constant keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var icno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityCancelBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        icno = preferences.getString("icno_key", null)

        db = FirebaseFirestore.getInstance()

        val bookingid = intent.getStringExtra("bookingid")

        db.collection("Booking").whereEqualTo("booking_id", bookingid).get()
            .addOnSuccessListener { doc ->
                for (document in doc) {
                    binding.txtCompanyName.text = document.get("rehab_name").toString()
                    binding.txtTreatmentPlan.text = document.get("treatment_plan").toString()
                    binding.txtRehabDate.text = document.get("booking_date").toString()
                    binding.txtTime.text = document.get("booking_time").toString()
                }
            }

        //setting adapter
        val spinner: Spinner = binding.reasonSpinner

        ArrayAdapter.createFromResource(
            this,
            R.array.reason_array,
            android.R.layout.simple_spinner_item
        ).also {
            //specify the layout to use when the list of choices appears
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //apply the adapter to the spinner
            spinner.adapter = adapter
        }

        binding.saveBtn.setOnClickListener {
            val status = "Canceled"
            db.collection("Booking").document(bookingid.toString())
                .update(
                    mapOf(
                        "booking_status" to status,
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Booking is canceled", Toast.LENGTH_SHORT).show()
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