package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
//import com.example.ohxinli.data.Result
import com.example.ohxinli.databinding.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class paymentCard : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentCardBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.getExtras();
        val regis_no = extras?.getString("regis_no")
        val bookingID = extras?.getString("booking_id")
        val icNo = extras?.getString("icNo")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_card)
        getSupportActionBar()?.hide()

        binding = ActivityPaymentCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.savebtn.setOnClickListener {
            // if(mCheckBox.isChecked) {
            //updateCard(icNo)

            //intent = Intent(this, EditUser::class.java)

            Toast.makeText(
                applicationContext,
                "Payment Successfully",
                Toast.LENGTH_SHORT
            ).show()
            intent = Intent(this, myBooking::class.java)
            startActivity(intent)
        }
    }

}

//    fun updateCard(icNo:String?){
//
//        val cardHolderName = binding.txtcardHolder.text.toString()
//        val cardNo = binding.txtCardNo.text.toString()
//        val expirymm = binding.mm.text.toString()
//        val expiryyy = binding.yy.text.toString()
//        val cvv = binding.cvv.text.toString()
//
//        db.collection("payment").document(icNo.toString())
//            .update(
//                mapOf(
//                    "cardHolderName" to cardHolderName,
//                    "cardNo" to cardNo,
//                    "expirymm" to expirymm,
//                    "expiryyy" to expiryyy,
//                    "cvv" to cvv
//                )
//            )
//            .addOnSuccessListener {
//                Toast.makeText(applicationContext, "Card Saved", Toast.LENGTH_SHORT).show()
//            }
//
//    }
//
//    fun displayCard(icNo:String?){
//        db = FirebaseFirestore.getInstance()
//        val addOnSuccessListener = db.collection("payment").whereEqualTo("icNo", icNo).get()
//            .addOnSuccessListener { doc ->
//                for (document in doc) {
//                    binding.txtCardNo.setText(document.get("cardNo").toString())
//                    binding.txtcardHolder.setText(document.get("cardHolderName").toString())
//                    binding.cvv.setText(document.get("cvv").toString())
//                    binding.yy.setText(document.get("expiryyy").toString())
//                    binding.mm.setText(document.get("expirymm").toString())
//
//
//                }
//            }
//    }


//}
