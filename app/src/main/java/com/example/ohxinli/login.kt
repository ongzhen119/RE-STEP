package com.example.ohxinli

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.ohxinli.databinding.ActivityCreateUserBinding
import com.example.ohxinli.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class login<override> : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    //Action Bar
    private  lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var  progressDialog: ProgressDialog

    //firebase
    private lateinit var firebaseAuth: FirebaseAuth

    private var email =""
    private var password =""
    //creating constant keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null
    var icno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth  = FirebaseAuth.getInstance()
        //checkUser()

        db = FirebaseFirestore.getInstance()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        regisno = preferences.getString("regisno_key", null)
        icno = preferences.getString("icno_key", null)

        binding.loginButton.setOnClickListener {

            var emailDB: String = ""
            var passwordDB: String = ""

            val emailInput = binding.loginUser.text.toString()
            val passwordInput = binding.loginPassword.text.toString()

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

            if(!rfound){
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
            else if((!ufound && rfound) || (ufound && !rfound)){
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

    private fun checkUser() {
        val preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        //get current user
        val firebaseUser =  firebaseAuth.currentUser

        if(firebaseUser != null){
            //user is already log out
                // need to retrieve db check email type

            var emailDB: String = ""
            var passwordDB: String = ""

            val emailInput = binding.loginUser.text.toString()
            val passwordInput = binding.loginPassword.text.toString()

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
                                ufound = true
                                val editor: SharedPreferences.Editor = preferences.edit()
                               editor.putString("icno_key", ic_no)
                                editor.apply()

                                intent = Intent(this, usermain::class.java)
                                startActivity(intent)
                            }
                        }
                        ufound = true
                    }
            }
            else if((!ufound && rfound) || (ufound && !rfound)){
                Toast.makeText(applicationContext, "Incorrect email and/or password", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
