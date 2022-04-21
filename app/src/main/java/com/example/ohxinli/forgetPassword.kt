package com.example.ohxinli

//data retrieve issue, zen yang dou tiao bu jin na ge loop
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.app.AlertDialog
import android.app.Notification
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.ohxinli.databinding.ActivityForgetPasswordBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class forgetPassword<override> : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()

        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.createnewBtn.setOnClickListener() {
            intent = Intent(this, create_user::class.java)
            startActivity(intent)
        }

        db = FirebaseFirestore.getInstance()

        binding.resetbtn.setOnClickListener() {
            var emailInput = binding.email.text.toString()
            var rfound = 0
            var ufound = 0
            var emailDB: String
            var userEmailDB: String

            db.collection("RehabCenter").whereEqualTo("rehab_email", emailInput).get()
                .addOnSuccessListener { doc ->
                    for (document in doc) {
                        if (emailInput == document.get("rehab_email").toString()) {
                            rfound = 1
                            Toast.makeText(
                                applicationContext,
                                "Rehab forget password",
                                Toast.LENGTH_SHORT
                            ).show()
                            intent = Intent(this, resetPassword::class.java)
                            intent.putExtra("email", emailInput)
                            startActivity(intent)
                            break
                        }
                    }
                }


            db.collection("User").whereEqualTo("user_email", emailInput).get()
                .addOnSuccessListener { doc ->
                    for (document in doc) {
                        if (emailInput == document.get("user_email").toString()) {
                            ufound = 1
                            Toast.makeText(
                                applicationContext,
                                "User forget password",
                                Toast.LENGTH_SHORT
                            ).show()
                            intent = Intent(this, resetPassword::class.java)
                            intent.putExtra("email", emailInput)
                            startActivity(intent)
                            break
                        }
                    }
                }

//            if (ufound == 0 && rfound == 0) {
//                Toast.makeText(
//                    applicationContext,
//                    "Invalid",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }


        }

//            db.collection("RehabCenter").get()
//                .addOnSuccessListener { doc ->
//                    loop@ for (document in doc) {
//                        emailDB = document.get("rehab_email").toString()
//
//                        if (emailDB == emailInput) {
////                            ufound = false
//                        Toast.makeText(
//                            applicationContext,
//                            "Rehab forget password",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        intent = Intent(this, resetPassword::class.java)
//                        intent.putExtra("email", emailInput)
//                        startActivity(intent)
//                        break@loop
//                        }
//                    }
//                    rfound = true
//                }
//            if(rfound == false) {
//                db.collection("User").whereEqualTo("user_email", emailInput).get()
//                    .addOnSuccessListener { doc ->
//                        for (document in doc) {
//                            userEmailDB = document.get("user_email").toString()
//                            if (userEmailDB == emailInput) {
//                                ufound = true
////                                rfound = false
//                                Toast.makeText(
//                                    applicationContext,
//                                    "User forget password",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                intent = Intent(this, resetPassword::class.java)
//                                intent.putExtra("email", emailInput)
//                                startActivity(intent)
//                                break
//                            }
//                        }
//                        ufound = true
//                    }
//            }
//            if(rfound == false && ufound == false)
//                Toast.makeText(applicationContext, "Invalid E-mail. Please try again!", Toast.LENGTH_SHORT).show()
//
//
//                        }
//                    }


        //rfound = true


//            if(rfound == false){
//                db.collection("User").get()
//                    .addOnSuccessListener { doc ->
//                        for(document in doc){
//                            userEmailDB = document.get("user_email").toString()
//                            if(userEmailDB == emailInput){
//                                ufound == true
////                                rfound = false
//                                Toast.makeText(applicationContext, "User forget password", Toast.LENGTH_SHORT).show()
//                                intent = Intent(this, resetPassword::class.java)
//                                intent.putExtra("email",emailInput)
//                                startActivity(intent)
//                            }
//                        }
//                    }
////                ufound = true
//            }

//            if(rfound == false && ufound == false) {
//                Toast.makeText(applicationContext, "Invalid E-mail. Please try again!", Toast.LENGTH_SHORT).show()
//            }


        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }
        }

    }
}
