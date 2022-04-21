package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.RadioButton
import com.example.ohxinli.databinding.ActivityEditRehabBinding
import com.example.ohxinli.databinding.ActivityPaymentBinding
import com.example.ohxinli.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class payment : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.getExtras();
        val regis_no = extras?.getString("regisno_key")
        val bookingID = extras?.getString("booking_id_key")
        val icNo = extras?.getString("icNo_key")

//        val regis_no = "201901223766"
//        val bookingID = "0"
//        val icNo = "010206-07-0995"

        getSupportActionBar()?.hide()

        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //display information
        displayInfo(bookingID, regis_no)

        val rb_bank = binding.rbbank
        val rb_card = binding.rbcard

        binding.savebtn.setOnClickListener{
            if(rb_bank.isChecked) {

                intent.putExtra("regis_no", regis_no)
                intent.putExtra("booking_id", bookingID)

                intent = Intent(this,paymentBank::class.java)
                startActivity(intent)
            }
            else if(rb_card.isChecked) {
                intent.putExtra("regis_no", regis_no)
                intent.putExtra("booking_id", bookingID)
                intent.putExtra("icNo",icNo)
                intent = Intent(this,paymentCard::class.java)
                startActivity(intent)
            }

        }

    }

    fun displayInfo(bookingID: String?, regis_no: String?) {

        db = FirebaseFirestore.getInstance()

        db.collection("Booking").whereEqualTo("booking_id", bookingID).get()
            .addOnSuccessListener { doc ->
                for (document in doc) {
                    binding.txtCompanyName.setText(document.get("rehab_name").toString())
                    binding.txtTreatmentPlan.setText(document.get("treatment_plan").toString())
                    binding.txtRehabDate.setText(document.get("booking_date").toString())
                    binding.txtTime.setText(document.get("booking_time").toString())
                    binding.txtTotal.setText(document.get("treatment_price").toString())

                }
            }

    }

}