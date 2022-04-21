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
import androidx.appcompat.app.ActionBar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ohxinli.databinding.ActivityScheduleBinding
import com.google.firebase.firestore.FirebaseFirestore

class schedule : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var db: FirebaseFirestore

    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        regisno = preferences.getString("regisno_key", null)

        db = FirebaseFirestore.getInstance()

        getBookingData()

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    private fun getBookingData() {
        var userIc: String
        var name = ""
        val scheduleList: ArrayList<BookingClass4> = ArrayList()
        var status = ""

        db.collection("Booking").get()
            .addOnSuccessListener { doc->

                for(document in doc){
                    val bookingDate = document.get("booking_date").toString()
                    val bookingTime = document.get("booking_time").toString()
                    val session = document.get("session").toString()
                    userIc = document.get("user_ic").toString() //check null

//                                name = document.get("name").toString()
                    var schedule = BookingClass4(bookingDate, bookingTime, session.toInt(), name, userIc, status)

                    scheduleList.add(schedule)
//                            }
//                        }
//                        .addOnFailureListener {
//                            Log.e("Firebase", "Error loading file ${it.toString()}")
//                        }

                    var scheduleRecyclerView = binding.scheduleList
                    scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
                    scheduleRecyclerView.adapter = ScheduleAdapter(scheduleList)
                }
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error loading file ${it.toString()}")
            }

//            db.collection("User").whereEqualTo("ic_no", "userIc").get()
//                .addOnSuccessListener { doc ->
//                    for(document in doc){
//                        val name = document.get("name").toString()
//                    }
//                    var bookingRequestRecyclerView = binding.requestList
//                }


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

//    fun updateStatus() {
//
//        val status = "Pending Session"
//
//        db.collection("Booking").document(treatment_id.toString())
//            .update(
//                mapOf(
//                    "status" to status,
//                )
//            )
//            .addOnSuccessListener {
//                Toast.makeText(applicationContext, "Treatment Updated", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        intent = Intent(this, treatmentlist:: class.java)
//        startActivity(intent)
//
//    }


}