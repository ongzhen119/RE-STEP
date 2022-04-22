package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ohxinli.databinding.ActivityCreateUserBinding
import com.example.ohxinli.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class login<override> : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore

    //creating constant keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null
    var icno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()

        db = FirebaseFirestore.getInstance()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        regisno = preferences.getString("regisno_key", null)
        icno = preferences.getString("icno_key", null)

        binding.loginButton.setOnClickListener {

            var emailDB: String = ""
            var passwordDB: String = ""

            var emailInput = binding.loginUser.text.toString()
            var passwordInput = binding.loginPassword.text.toString()

            var rfound = false
            var ufound = false

            db.collection("RehabCenter").get()
                .addOnSuccessListener { doc ->
                    for(document in doc){
                        emailDB = document["rehab_email"].toString()
                        passwordDB = document["rehab_password"].toString()
                        if(emailDB == emailInput && passwordDB == passwordInput){
                            val regis_no = document["regis_no"].toString()
                            Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT).show()
//                            rfound = true
                            var editor: SharedPreferences.Editor = preferences.edit()
                            editor.putString("regisno_key", regis_no)

                            editor.apply()

                            intent = Intent(this, rehabmain::class.java)
                            startActivity(intent)
                        }
                    }
                    rfound = true
                }

            if(rfound == false){
                db.collection("User").get()
                    .addOnSuccessListener { doc ->
                        for(document in doc){
                            emailDB = document["user_email"].toString()
                            passwordDB = document["user_password"].toString()

                            if(emailDB == emailInput && passwordDB == passwordInput){
                                val ic_no = document["ic_no"].toString()
                                Toast.makeText(applicationContext, "Login successfully", Toast.LENGTH_SHORT).show()
                                //ufound = true
                                var editor: SharedPreferences.Editor = preferences.edit()
                                editor.putString("icno_key", ic_no)

                                editor.apply()

                                intent = Intent(this, usermain::class.java)
                                startActivity(intent)
                            }
                        }
                        ufound = true
                    }
            }
            else if((ufound == false && rfound == true) || (ufound == true && rfound == false)){
                Toast.makeText(applicationContext, "Incorrect email and/or password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createNew.setOnClickListener{
            intent = Intent(this, createnew_p1::class.java)
            startActivity(intent)
        }

        binding.forgetPassword.setOnClickListener{
            intent = Intent(this, forgetPassword::class.java)
            startActivity(intent)
        }
    }

}