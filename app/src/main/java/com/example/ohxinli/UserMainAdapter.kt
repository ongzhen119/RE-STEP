package com.example.ohxinli

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class UserMainAdapter(private var rehabList: ArrayList<RehabCenter2>) : RecyclerView.Adapter<UserMainAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    //setter
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    var tempRehabList = ArrayList<RehabCenter2>()

    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
        val companyName : TextView = view.findViewById(R.id.companyName)
        val address : TextView = view.findViewById(R.id.address)
        val website : TextView = view.findViewById(R.id.website)
        val phoneNum : TextView = view.findViewById(R.id.phoneNum)

        val imageView : ImageView = view.findViewById(R.id.rehabImageView)
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    init {
        tempRehabList = rehabList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usermain_item_view, parent, false)
        return ViewHolder((view), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rehabCenter = rehabList[position]
        val regisNo = rehabCenter.regis_no
        val localFile = File.createTempFile("tempImage", "jpg")
        val storageRef = FirebaseStorage.getInstance().reference.child("rehabImages/$regisNo.jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        }

        holder.companyName.text = rehabCenter.company_name
        holder.address.text = rehabCenter.address
        holder.website.text = rehabCenter.website
        holder.phoneNum.text = rehabCenter.phone_num
    }

    override fun getItemCount(): Int {
        return tempRehabList.size
    }
}