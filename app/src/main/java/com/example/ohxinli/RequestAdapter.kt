package com.example.ohxinli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RequestAdapter(private var requestList: ArrayList<BookingClass3>) : RecyclerView.Adapter<RequestAdapter.ViewHolder>(){

//    private lateinit var mListener: onItemClickListener
//
//    interface onItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    //setter
//    fun setOnItemClickListener(listener: onItemClickListener){
//        mListener = listener
//    }

    var tempRequestList = ArrayList<BookingClass3>()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val username : TextView = view.findViewById(R.id.name)
        val date : TextView = view.findViewById(R.id.date)
        val time : TextView = view.findViewById(R.id.time)
        val session : TextView = view.findViewById(R.id.sessionVal)

//        init {
//            view.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
//        }
    }

    init {
        tempRequestList = requestList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookingrequest_item_view, parent, false)
        return ViewHolder((view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var request = requestList[position]

        holder.username.text = request.name
        holder.date.text = request.booking_date
        holder.time.text = request.booking_time
        holder.session.text = request.session.toString()
    }

    override fun getItemCount(): Int {
        return tempRequestList.size
    }
}