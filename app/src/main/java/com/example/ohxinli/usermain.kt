package com.example.ohxinli

import android.content.Intent
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
import com.example.ohxinli.databinding.ActivityUsermainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User


class usermain : AppCompatActivity() {
    private lateinit var binding: ActivityUsermainBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsermainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        getRehabData()

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    private fun getRehabData() {

        val rehabList: ArrayList<RehabCenter2> = ArrayList()

        db.collection("RehabCenter").get()
            .addOnSuccessListener { doc->
                for(document in doc){
                    val regisno = document.get("regis_no").toString()
                    val companyName = document.get("company_name").toString()
                    val address = document.get("address").toString()
                    val website = document.get("website").toString() //check null
                    val tel = document.get("phone_no").toString()


                    val rehab = RehabCenter2(regisno, address, companyName, tel, website)
                    rehabList.add(rehab)


                    var userMainRecyclerView = binding.rehabList
                    userMainRecyclerView.layoutManager = LinearLayoutManager(this)
                    userMainRecyclerView.adapter = UserMainAdapter(rehabList)


                    var adapter = UserMainAdapter(rehabList)
                    userMainRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object: UserMainAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val regisno = rehabList[position].regis_no
                            //Toast.makeText(this@usermain, "You clicked on $regisno", Toast.LENGTH_SHORT).show()
                            intent = Intent(this@usermain, booking::class.java)
                            intent.putExtra("regisno_key", regisno)
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