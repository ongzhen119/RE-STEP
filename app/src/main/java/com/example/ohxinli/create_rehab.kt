package com.example.ohxinli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.ohxinli.databinding.ActivityCreateRehabBinding
import com.google.firebase.firestore.FirebaseFirestore

class create_rehab : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityCreateRehabBinding

    //creating constant keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setActionBar()

        var preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        getSupportActionBar()?.hide() // hide the title bar

        super.onCreate(savedInstanceState)

        binding = ActivityCreateRehabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        regisno = preferences.getString("regisno_key", null)

        binding.savebtn.setOnClickListener {

            val address = binding.address.text.toString()
            val company_name = binding.name.text.toString()
            val phone_no = binding.phone.text.toString()
            val regis_no = binding.ic.text.toString()
            val rehab_email = binding.email.text.toString()
            val rehab_password = binding.password1.text.toString()
            val confirmPassword = binding.password2.text.toString()
            val rehab_username = binding.username.text.toString()
            val website = binding.web.text.toString()

            if(address.isEmpty() || company_name.isEmpty() || phone_no.isEmpty() || regis_no.isEmpty() || rehab_email.isEmpty() || rehab_password.isEmpty() || rehab_username.isEmpty()){
                Toast.makeText(applicationContext, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            }else if(rehab_password != confirmPassword){
                Toast.makeText(applicationContext, "Confirm password is different from password", Toast.LENGTH_SHORT).show()
            }else{
                val rehab = RehabCenter(address, company_name, phone_no, regis_no, rehab_email, rehab_password, rehab_username, website)
                db.collection("RehabCenter").document("$regis_no").set(rehab)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Rehab Profile Created", Toast.LENGTH_SHORT).show()
                        var editor: SharedPreferences.Editor = preferences.edit()
                        editor.putString("regisno_key", regis_no)

                        editor.apply()

                        intent = Intent(this, rehabmain:: class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Log.w("Error", it)
                    }
            }
        }
    }

    fun setActionBar(){
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.abs_layout)
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

    }
}