package com.example.ohxinli

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.ohxinli.databinding.ActivityRehabmainBinding

class rehabmain : AppCompatActivity() {
    private lateinit var binding: ActivityRehabmainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide() //hide action bar
        super.onCreate(savedInstanceState)
        binding = ActivityRehabmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //profile
        binding.b1.setOnClickListener {
            intent = Intent(this, editRehab:: class.java)
            startActivity(intent)
        }

        //appointment request
        binding.b2.setOnClickListener {
            intent = Intent(this, booking_request:: class.java)
            startActivity(intent)
        }

        //schedule
        binding.b3.setOnClickListener {
            intent = Intent(this, schedule:: class.java)
            startActivity(intent)
        }

        //treatment management
        binding.b4.setOnClickListener {
            intent = Intent(this, treatmentlist:: class.java)
            startActivity(intent)
        }
    }

}