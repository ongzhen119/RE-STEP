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
import com.example.ohxinli.databinding.ActivityTreatmentlistBinding
import com.google.firebase.firestore.FirebaseFirestore

class treatmentlist : AppCompatActivity() {
    private lateinit var binding: ActivityTreatmentlistBinding
    private lateinit var db: FirebaseFirestore

    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

        binding = ActivityTreatmentlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        regisno = preferences.getString("regisno_key", null)
        db = FirebaseFirestore.getInstance()

        getTreatmentData()

        binding.addBtn.setOnClickListener{
            intent = Intent(this, addTreatment::class.java)
            startActivity(intent)
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    private fun getTreatmentData() {

        val treatmentList: ArrayList<Treatment2> = ArrayList()
        db.collection("Treatment").whereEqualTo("regis_no", regisno.toString()).get()
            .addOnSuccessListener { doc->
                for(document in doc){
                    val treatmentName = document.get("plan_name").toString()
                    val treatmentId = document.get("treatment_id").toString()

                    val treatment = Treatment2(treatmentName, treatmentId)
                    treatmentList.add(treatment)

                    var treatmentRecyclerView = binding.treatmentList
                    treatmentRecyclerView.layoutManager = LinearLayoutManager(this)
                    treatmentRecyclerView.adapter = TreatmentAdapter(treatmentList)

                    var adapter = TreatmentAdapter(treatmentList)
                    treatmentRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object: TreatmentAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val treatmentId = treatmentList[position].treatment_id
                            //Toast.makeText(this@usermain, "You clicked on $regisno", Toast.LENGTH_SHORT).show()
                            intent = Intent(this@treatmentlist, editTreatment::class.java)
                            intent.putExtra("treatment_key", treatmentId)
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