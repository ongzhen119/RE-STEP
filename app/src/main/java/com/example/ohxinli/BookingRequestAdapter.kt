package com.example.ohxinli
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class BookingRequestAdapter (private var bookingList: ArrayList<BookingClass2>) : RecyclerView.Adapter<BookingRequestAdapter.ViewHolder>(){
//    var tempBookingList = ArrayList<BookingClass2>()
//
//    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
//        val bookingDate : TextView = view.findViewById(R.id.date)
//        val bookingTime : TextView = view.findViewById(R.id.time)
//        val session : TextView = view.findViewById(R.id.sessionVal)
//        val user_ic : TextView = view.findViewById(R.id.name)
//    }
//
//    init {
//        tempBookingList = bookingList
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookingrequest_item_view, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var booking = bookingList[position]
//
//        holder.bookingDate.text = booking.booking_date
//        holder.bookingTime.text = booking.booking_time
//        holder.session.text = booking.session.toString()
//        holder.user_ic.text = booking.user_ic
//    }
//
//    override fun getItemCount(): Int {
//        return tempBookingList.size
//    }
//
//
//}