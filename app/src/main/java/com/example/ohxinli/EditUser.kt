package com.example.ohxinli

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ohxinli.databinding.ActivityEditUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File

class EditUser : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityEditUserBinding
    var TAKE_IMAGE_CODE = 10001

    //creating constand keys for shared preferences
    val SHARED_PREFS = "shared_prefs"
    var icno: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences: SharedPreferences =
            this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        icno = preferences.getString(
                "icno_key", null
        )
        val localFile = File.createTempFile("tempImage", "jpg")
        val storageRef = FirebaseStorage.getInstance().reference.child("profileImages/$icno.jpg")
        Log.d("test",storageRef.toString())
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageViewProfilePic.setImageBitmap(bitmap)
        }
        // Create a reference with an initial file path and name





        //display user information
        displayUserInfo()


        binding.savebtn.setOnClickListener {
            updateUserInfo()
            displayUserInfo()
        }
        binding.imageViewProfilePic.setOnClickListener {
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_IMAGE_CODE)
            }
        }

        val drawer: DrawerLayout = binding.drawerLayout
        setActionBar(drawer)
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMAGE_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val bitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
                    binding.imageViewProfilePic.setImageBitmap(bitmap)
                    handleUpload(bitmap)

                }
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val reference = FirebaseStorage.getInstance().reference
            .child("profileImages")
            .child("$icno.jpg")
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

    fun displayUserInfo() {
        db = FirebaseFirestore.getInstance()

        db.collection("User").whereEqualTo("ic_no", icno).get()
            .addOnSuccessListener { doc ->
                for (document in doc) {
                    binding.username.setText(document.get("username").toString())
                    binding.name.setText(document.get("name").toString())
                    binding.phone.setText(document.get("phone_no").toString())
                    binding.email.setText(document.get("user_email").toString())
                    binding.ic.setText(document.get("ic_no").toString())
                    binding.yrs.setText(document.get("drug_duration").toString())
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error Loading File: ${it.toString()}")
            }
    }

    fun updateUserInfo() {

        val username = binding.username.text.toString()
        val name = binding.name.text.toString()
        val phone_no = binding.phone.text.toString()
        val drug_duration = binding.yrs.text.toString()
        val user_email = binding.email.text.toString()
        val ic_no = binding.ic.text.toString()

        db.collection("User").document(ic_no)
            .update(
                mapOf(
                    "username" to username,
                    "name" to name,
                    "phone_no" to phone_no,
                    "drug_duration" to drug_duration,
                    "user_email" to user_email
                )
            )
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "User Profile Updated", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun setActionBar(drawer: DrawerLayout) {

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
                    intent = Intent(this, usermain::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    intent = Intent(this, EditUser::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_myBookings -> {
                    intent = Intent(this, myBooking::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    intent = Intent(this, login::class.java)
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