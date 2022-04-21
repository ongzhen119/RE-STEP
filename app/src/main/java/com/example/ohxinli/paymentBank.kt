package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.ohxinli.databinding.ActivityBookingBinding
import com.example.ohxinli.databinding.ActivityPaymentBankBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class paymentBank : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_bank)
        getSupportActionBar()?.hide()

        binding = ActivityPaymentBankBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.savebtn.setOnClickListener {
            val tvResult: TextView = binding.tvResult
            val tvResultString: String = tvResult.text.toString()
            val radioGroup: RadioGroup = binding.bankMethodRg
            if (radioGroup.checkedRadioButtonId != null) {
                when (radioGroup.checkedRadioButtonId) {
                    R.id.rb_pbb -> tvResult.text = tvResultString + "You have selected Public Bank"
                    R.id.rb_mbb -> tvResult.text = tvResultString + "You have selected MayBank"
                    R.id.rb_cimb -> tvResult.text = tvResultString + "You have selected CIMB Bank"
                    R.id.rb_bsn -> tvResult.text = tvResultString + "You have selected Bank Simpanan Nasional"
                    R.id.rb_affin -> tvResult.text = tvResultString + "You have selected Affin Bank"
                    R.id.rb_ambank -> tvResult.text = tvResultString + "You have selected Ambank"
                    R.id.rb_hsbc -> tvResult.text = tvResultString + "You have selected HSBC Bank"
                    R.id.rb_rhb -> tvResult.text = tvResultString + "You have selected RHB Bank"
                    R.id.rb_uob -> tvResult.text = tvResultString + "You have selected UOB Bank"
                    R.id.rb_sc -> tvResult.text = tvResultString + "You have selected Standard Chartered Bank"
                    R.id.rb_agro -> tvResult.text = tvResultString + "You have selected AgroBank"
                    R.id.rb_ocbc -> tvResult.text = tvResultString + "You have selected OCBC Bank"
                    R.id.rb_mutmalat -> tvResult.text = tvResultString + "You have selected Bank Mutlamat"
                    R.id.rb_alliance -> tvResult.text = tvResultString + "You have selected Alliance Bank"

                }
            }
            Toast.makeText(applicationContext, tvResultString, Toast.LENGTH_SHORT).show()
            intent = Intent(this, myBooking::class.java)
            startActivity(intent)
        }
    }
}

