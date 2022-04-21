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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ohxinli.databinding.ActivityMyBookingBinding
import com.google.firebase.firestore.FirebaseFirestore

class myBooking : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityMyBookingBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var icno: String? = null

    private lateinit var recordRecyclerview: RecyclerView
    private lateinit var recordArrayList: ArrayList<BookingClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
//        getSupportActionBar()?.hide() //hide title bar

        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        icno = preferences.getString("icno_key", null)

        db = FirebaseFirestore.getInstance()

        recordRecyclerview = binding.bookingList
        recordRecyclerview.layoutManager = LinearLayoutManager(this)
        recordRecyclerview.setHasFixedSize(true)

        recordArrayList = arrayListOf<BookingClass>()
        getdata()

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    private fun getdata() {
        val bookingList: ArrayList<BookingClass2> = ArrayList()

        db.collection("Booking").whereEqualTo("user_ic", icno).get()
            .addOnSuccessListener { doc ->
                for (document in doc) {
                    val bookingid = document.get("booking_id").toString()
                    val rehabName = document.get("rehab_name").toString()
                    val treatmentPlan = document.get("treatment_plan").toString()
                    val bookingDate = document.get("booking_date").toString()
                    val bookingTime = document.get("booking_time").toString()
                    val bookingStatus = document.get("booking_status").toString()

                    val booking = BookingClass2(bookingid, bookingDate, bookingTime, rehabName, treatmentPlan, bookingStatus)
                    bookingList.add(booking)

                    var bookingRecyclerView = binding.bookingList
                    bookingRecyclerView.layoutManager = LinearLayoutManager(this)
                    bookingRecyclerView.adapter = myBookingAdapter(bookingList)

                    var adapter = myBookingAdapter(bookingList)
                    bookingRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object: myBookingAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val bookingid = bookingList[position].booking_id
                            Toast.makeText(this@myBooking, "You clicked on $bookingid", Toast.LENGTH_SHORT).show()
                            intent = Intent(this@myBooking, viewBookingDetails::class.java)
                            intent.putExtra("rehabname_key", rehabName)
                            intent.putExtra("bookingid_key", bookingid)
                            startActivity(intent)
                        }
                    })
                }
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error loading file ${it.toString()}")
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