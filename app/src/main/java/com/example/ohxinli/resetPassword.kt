package com.example.ohxinli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.ohxinli.databinding.ActivityResetPasswordBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import android.widget.Toast
import com.example.ohxinli.databinding.ActivityForgetPasswordBinding

class resetPassword : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        getSupportActionBar()?.hide()

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val email=intent.getStringExtra("email")
        val p1 = binding.pwd1.text.toString()
        val p2 = binding.pwd2.text.toString()

        updateDB(email, p1)
    }

    fun updateDB(email:String?,p1:String?) {
        var rfound = false
        db = FirebaseFirestore.getInstance()


        db.collection("RehabCenter").get()
            .addOnSuccessListener { doc ->
                for (document in doc) {
                    if (email == document["rehab_email"].toString()) {
                        db.collection("RehabCenter").document(email)
                            .update(
                                mapOf("rehab_email" to email)
                            )
                            .addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Password Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        rfound = true
                        intent = Intent(this, login::class.java)
                        startActivity(intent)
                    }
                }
            }

        if (rfound == false) {
            db.collection("User").get()
                .addOnSuccessListener { doc ->
                    for (document in doc) {
                        if (email == document["user_email"].toString()) {
                            db.collection("User").document(email)
                                .update(
                                    mapOf("user_email" to email)
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Password Updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            rfound = true
                            intent = Intent(this, login::class.java)
                            startActivity(intent)
                        }
                    }
                }
        }


    }
}