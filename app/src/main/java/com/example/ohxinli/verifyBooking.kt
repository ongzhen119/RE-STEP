package com.example.ohxinli

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ohxinli.databinding.ActivityVerifyBookingBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class verifyBooking: AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityVerifyBookingBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"

    var icno: String? = null

    //calendar
    val myCalendar: Calendar? = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd/MM/yy HH:mm a", Locale.ENGLISH)
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences =
            this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()

        binding = ActivityVerifyBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookingid = intent.getStringExtra("booking_id_key")
        val rehabRegisno = intent.getStringExtra("regisno_key")


        displayRehab(rehabRegisno)
        displaySelectedTreatment(bookingid)

        //choose another session
        binding.submitBtn.setOnClickListener {
            openDateDialog()
        }

        //store into db
        binding.savebtn.setOnClickListener {
            updateDate(bookingid)
            intent = Intent(this, payment:: class.java)
            val extras = Bundle()
            extras.putString("booking_id_key",bookingid)
            extras.putString("regisno_key",rehabRegisno)
            intent.putExtras(extras)
            startActivity(intent)
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    fun displayRehab(rehabRegisno: String?) {
        db.collection("RehabCenter").whereEqualTo("regis_no", rehabRegisno.toString()).get()
            .addOnSuccessListener { doc ->
                for (document in doc) {
                    binding.txtCompanyName.text = document.get("company_name").toString()
                    binding.txtAddress.text = document.get("address").toString()
                    binding.txtWebsite.text = document.get("website").toString()
                    binding.txtTel.text = document.get("phone_no").toString()
                }
            }
    }

    fun displaySelectedTreatment(bookingid: String?) {
        val docRef = db.collection("Booking").document(bookingid.toString())
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                binding.txtTreatmentPlan.text = snapshot.get("treatment_plan").toString()
                binding.txtRehabPrice.text = snapshot.get("treatment_price").toString()
                binding.txtRehabDate.text = snapshot.get("booking_date").toString()
                binding.txtTime.text = snapshot.get("booking_time").toString()
            }
        }
    }

    fun openDateDialog() {
        val dateUI = binding.txtRehabDate.text.toString()
        val timeUI = binding.txtTime.text.toString()

        val dateee = "$dateUI $timeUI"
        myCalendar?.setTime(sdf.parse(dateee))

        var editDateText = binding.txtRehabDate
        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalendar!![Calendar.YEAR] = year
                myCalendar!![Calendar.MONTH] = month
                myCalendar!![Calendar.DAY_OF_MONTH] = day
                val myFormat = "dd/MM/yy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                editDateText.setText(dateFormat.format(myCalendar.time))
                openTimeDialog()
            }
        DatePickerDialog(
            this,
            date,
            myCalendar!![Calendar.YEAR],
            myCalendar!![Calendar.MONTH],
            myCalendar!![Calendar.DAY_OF_MONTH]
        ).show()
    }

    fun openTimeDialog() {
        var editTimeText = binding.txtTime
        val time =
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hour: Int, mins: Int ->
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
                editTimeText.setText(formattedTime)
            }

        TimePickerDialog(
            this,
            time,
            myCalendar!![Calendar.HOUR],
            myCalendar!![Calendar.MINUTE],
            false
        ).show()

    }

    fun updateDate(bookingid: String?) {
        val date = binding.txtRehabDate.text.toString()
        val time = binding.txtTime.text.toString()

        db.collection("Booking").document(bookingid.toString())
            .update(
                mapOf(
                    "booking_date" to date,
                    "booking_time" to time
                )
            )
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Booking Confirmed", Toast.LENGTH_SHORT)
                    .show()
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
        menu.visibility = View.GONE
    }

}

