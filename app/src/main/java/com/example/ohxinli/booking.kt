package com.example.ohxinli

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ohxinli.databinding.ActivityBookingBinding

import com.google.firebase.firestore.FirebaseFirestore

import android.R
import android.util.Log
import android.view.View

import java.util.*
import kotlin.collections.ArrayList
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.ActionBar

import java.text.SimpleDateFormat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class booking: AppCompatActivity()  {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityBookingBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"

    var rehabName: String? = null
    var icno: String? = null
    var session: Int = 0

    //calendar
    val myCalendar: Calendar? = Calendar.getInstance()
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?){
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        icno = preferences.getString("icno_key", null)
        
        val rehabRegisno = intent.getStringExtra("regisno_key") //get from intent
        val regisno = preferences.getString("regisno_key", null)
        val localFile = File.createTempFile("tempImage", "jpg")
        val storageRef = FirebaseStorage.getInstance().reference.child("rehabImages/$regisno.jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.rehabImg.setImageBitmap(bitmap)
        }
        //display rehab information
        displayRehab(rehabRegisno)

        //display treatment plan available
        displayTreatment(rehabRegisno)

        //display date
        selectDate()

        //display time
        selectTime()

        //store into db
        binding.saveBtn.setOnClickListener{
            val treatmentPlan = binding.spinnerTreatmentPlan.selectedItem.toString()
            val price = binding.txtRehabPrice.text.toString()
            val date = binding.txtRehabDate.text.toString()
            val time = binding.txtTime.text.toString()
            val status = "Pending Session"

            if(date == "Select Date"){
                Toast.makeText(applicationContext, "Please select date", Toast.LENGTH_SHORT).show()
            }else if(time == "Select Time"){
                Toast.makeText(applicationContext, "Please select time", Toast.LENGTH_SHORT).show()
            }else{
                val booking = BookingClass(
                    date,
                    time,
                    session,
                    rehabName,
                    treatmentPlan,
                    price.toInt(),
                    icno,
                    status
                )

                var bookingId: String = ""

                db.collection("Booking").add(booking)
                    .addOnSuccessListener { doc ->
                        bookingId = doc.id
                        Log.d(TAG, "DocumentSnapshot written with ID: ${doc.id}")
                        Toast.makeText(
                            applicationContext,
                            "Booking Submitted",
                            Toast.LENGTH_SHORT
                        ).show()

                        db.collection("Booking").document(bookingId)
                            .update(
                                mapOf(
                                    "booking_id" to bookingId,
                                )
                            )
                            .addOnSuccessListener {
                                intent = Intent(this, verifyBooking::class.java)
                                intent.putExtra("booking_id_key", bookingId)
                                intent.putExtra("regisno_key", rehabRegisno)
                                startActivity(intent)
                            }

                    }

            }

        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)

    }

    fun displayRehab(rehabRegisno: String?){
        db = FirebaseFirestore.getInstance()

        db.collection("RehabCenter").whereEqualTo("regis_no", rehabRegisno).get()
            .addOnSuccessListener { doc ->
                for(document in doc) {
                    binding.txtCompanyName.text = document.get("company_name").toString()
                    binding.txtAddress.text = document.get("address").toString()
                    binding.txtWebsite.text = document.get("website").toString()
                    binding.txtPhone.text = document.get("phone_no").toString()
                    rehabName = document.get("company_name").toString()
                }
            }
    }

    fun displayTreatment(rehabRegisno: String?){
        db = FirebaseFirestore.getInstance()

        val spinner: Spinner = binding.spinnerTreatmentPlan
        val treatment: MutableList<String?> = ArrayList()
        val adapter = ArrayAdapter(applicationContext, R.layout.simple_spinner_item, treatment)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;

        //get plan name into spinner
        db.collection("Treatment").whereEqualTo("regis_no", rehabRegisno).get()
            .addOnSuccessListener { doc ->
                for(document in doc){
                    val subject = document.getString("plan_name")
                    treatment.add(subject)
                }
                adapter.notifyDataSetChanged()
            }


        //get price based on plan name
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                db.collection("Treatment").whereEqualTo("plan_name", spinner.selectedItem.toString()).get()
                    .addOnSuccessListener { doc ->
                        for(document in doc){
                            binding.txtRehabPrice.text = document.get("price").toString()
                            session = document.get("treatment_session").toString().toInt()
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Error Loading File: ${it.toString()}")
                    }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(applicationContext, "Please select a treatment plan", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun selectDate(){
        var editText = binding.txtRehabDate
        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar!![Calendar.YEAR] = year
                myCalendar!![Calendar.MONTH] = month
                myCalendar!![Calendar.DAY_OF_MONTH] = day
                val myFormat = "dd/MM/yy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                editText.setText(dateFormat.format(myCalendar.time))
            }
        editText!!.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                myCalendar!![Calendar.YEAR],
                myCalendar!![Calendar.MONTH],
                myCalendar!![Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

    fun selectTime(){
        var editText = binding.txtTime
        val time = 
            OnTimeSetListener { timePicker: TimePicker, hour: Int, mins: Int ->
                val formattedTime: String = when {
                    hour == 0 -> {
                        if (mins < 10) {
                            "${hour + 12}:0${mins} am"
                        } else {
                            "${hour + 12}:${mins} am"
                        }
                    }
                    hour > 12 -> {
                        if (mins < 10) {
                            "${hour - 12}:0${mins} pm"
                        } else {
                            "${hour - 12}:${mins} pm"
                        }
                    }
                    hour == 12 -> {
                        if (mins < 10) {
                            "${hour}:0${mins} pm"
                        } else {
                            "${hour}:${mins} pm"
                        }
                    }
                    else -> {
                        if (mins < 10) {
                            "${hour}:${mins} am"
                        } else {
                            "${hour}:${mins} am"
                        }
                    }
                }
                myCalendar!![Calendar.HOUR] = hour
                myCalendar!![Calendar.MINUTE] = mins
                editText.setText(formattedTime)
            }
        editText!!.setOnClickListener {
            TimePickerDialog(
                this,
                time,
                myCalendar!![Calendar.HOUR],
                myCalendar!![Calendar.MINUTE],
                false
            ).show()
        }

    }

    fun setActionBar(drawer: DrawerLayout){

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(com.example.ohxinli.R.layout.abs_layout)
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
//        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.menu_icon)
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        val menu: ImageButton = findViewById(com.example.ohxinli.R.id.menu_icon)

        menu.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)
        }
    }
}