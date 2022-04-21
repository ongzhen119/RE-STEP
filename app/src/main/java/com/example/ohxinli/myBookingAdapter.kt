package com.example.ohxinli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myBookingAdapter(private var bookingList: ArrayList<BookingClass2>) : RecyclerView.Adapter<myBookingAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    //setter
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    var tempBooking = ArrayList<BookingClass2>()

    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
        val rehabName : TextView = view.findViewById(R.id.txtCompanyName)
        val treatmentPlan : TextView = view.findViewById(R.id.txtTreatmentPlan)
        val bookingDate : TextView = view.findViewById(R.id.txtRehabDate)
        val bookingTime : TextView = view.findViewById(R.id.txtTime)
        val bookingStatus : TextView = view.findViewById(R.id.txtStatus)

        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    init {
        tempBooking = bookingList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_booking_item_view, parent, false)
        return ViewHolder((view), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var booking = bookingList[position]

        holder.rehabName.text = booking.rehab_name
        holder.treatmentPlan.text = booking.treatment_plan
        holder.bookingDate.text = booking.booking_date
        holder.bookingTime.text = booking.booking_time
        holder.bookingStatus.text = booking.booking_status
    }

    override fun getItemCount(): Int {
        return tempBooking.size
    }

}