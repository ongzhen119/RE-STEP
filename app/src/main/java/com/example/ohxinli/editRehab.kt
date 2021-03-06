package com.example.ohxinli

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ohxinli.databinding.ActivityEditRehabBinding
import com.google.firebase.firestore.FirebaseFirestore
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File


class editRehab : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityEditRehabBinding

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var regisno: String? = null
    var TAKE_IMAGE_CODE = 10001

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences: SharedPreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityEditRehabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        regisno = preferences.getString("regisno_key", null)
        val localFile = File.createTempFile("tempImage", "jpg")
        val storageRef = FirebaseStorage.getInstance().reference.child("rehabImages/$regisno.jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.rehabImageView.setImageBitmap(bitmap)
        }


        //display information
        displayInfo(regisno)

        binding.savebtn.setOnClickListener{
            updateInfo()
            displayInfo(regisno)
        }
        binding.rehabImageView.setOnClickListener {
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_IMAGE_CODE)
            }
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    fun displayInfo(regisno: String?){
        db = FirebaseFirestore.getInstance()

        db.collection("RehabCenter").whereEqualTo("regis_no", regisno).get()
            .addOnSuccessListener { doc ->
                for(document in doc){
                    binding.username.setText(document.get("rehab_username").toString())
                    binding.name.setText(document.get("company_name").toString())
                    binding.phone.setText(document.get("phone_num").toString())
                    binding.email.setText(document.get("rehab_email").toString())
                    binding.ic.setText(document.get("regis_no").toString())
                    binding.address.setText(document.get("address").toString())
                    binding.web.setText(document.get("website").toString())
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error Loading File: ${it.toString()}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMAGE_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val bitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
                    binding.rehabImageView.setImageBitmap(bitmap)
                    handleUpload(bitmap)

                }
            }
        }
    }
    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val reference = FirebaseStorage.getInstance().reference
            .child("rehabImages")
            .child("$regisno.jpg")
        reference.putBytes(baos.toByteArray())
            .addOnSuccessListener { getDownloadUrl(reference) }
            .addOnFailureListener { e -> Log.e(ContentValues.TAG, "onFailure", e.cause) }
    }
    private fun getDownloadUrl(reference: StorageReference) {
        reference.downloadUrl
            .addOnSuccessListener { uri ->
                Log.e(ContentValues.TAG, "onSuccess$uri")
                setUserProfileUrl(uri)
            }
    }
    private fun setUserProfileUrl(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        user!!.updateProfile(request)
            .addOnSuccessListener { }
            .addOnFailureListener {
                //Toast.makeText(a.this, "Profile", Toast.LENGTH_SHORT).show();
            }
    }

    fun updateInfo() {

        val address = binding.address.text.toString()
        val company_name = binding.name.text.toString()
        val phone_no = binding.phone.text.toString()
        val regis_no = binding.ic.text.toString()
        val rehab_email = binding.email.text.toString()
        val rehab_username = binding.username.text.toString()
        val website = binding.web.text.toString()

        if(address.isEmpty() || company_name.isEmpty() || phone_no.isEmpty() || regis_no.isEmpty() || rehab_email.isEmpty() || rehab_username.isEmpty()){
            Toast.makeText(applicationContext, "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
        }else {
            db.collection("RehabCenter").document(regis_no)
                .update(
                    mapOf(
                        "address" to address,
                        "company_name" to company_name,
                        "phone_num" to phone_no,
                        "rehab_email" to rehab_email,
                        "rehab_username" to rehab_username,
                        "website" to website
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Rehab Profile Updated", Toast.LENGTH_SHORT)
                        .show()
                }
        }
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

        val navView = binding.navView
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    intent = Intent(this, rehabmain:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_treatment -> {
                    intent = Intent(this, treatmentlist:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_appointment -> { //change
                    intent = Intent(this, booking_request:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_schedule -> { //change
                    intent = Intent(this, schedule:: class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    intent = Intent(this, login:: class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

}