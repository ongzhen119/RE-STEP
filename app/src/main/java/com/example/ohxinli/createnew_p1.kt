package com.example.ohxinli

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ohxinli.databinding.ActivityCreateUserBinding
import com.example.ohxinli.databinding.ActivityCreatenewP1Binding
import android.media.Image as Image1

class createnew_p1 : AppCompatActivity() {
    private lateinit var binding: ActivityCreatenewP1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatenewP1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createrehab.setOnClickListener {
            intent = Intent(this, create_rehab:: class.java)
            startActivity(intent)
        }

        binding.createuser.setOnClickListener {
            intent = Intent(this, create_user:: class.java)
            startActivity(intent)
        }

        binding.createNew.setOnClickListener {
            intent = Intent(this, login:: class.java)
            startActivity(intent)
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)

    }

    fun setActionBar(drawer: DrawerLayout){

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.abs_layout)
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
//        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.menu_icon)
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        val menu: ImageButton = findViewById(R.id.menu_icon)

        menu.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)
        }
    }


}