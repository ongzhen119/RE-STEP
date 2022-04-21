package com.example.ohxinli
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//
//class BookingAdapter(private var bookingList: ArrayList<BookingClass2>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>(){
//
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
//
//    var tempBookingList = ArrayList<BookingClass2>()
//
//    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
////        val companyName : TextView = view.findViewById(R.id.companyName)
////        val address : TextView = view.findViewById(R.id.address)
////        val website : TextView = view.findViewById(R.id.website)
////        val phoneNum : TextView = view.findViewById(R.id.phoneNum)
//
//        init {
//            view.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
//        }
//    }
//
//    init {
//        tempBookingList = bookingList
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookingrequest_item_view, parent, false)
//        return ViewHolder((view), mListener)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var  = rehabList[position]
//
//        holder.companyName.text = rehabCenter.company_name
//        holder.address.text = rehabCenter.address
//        holder.website.text = rehabCenter.website
//        holder.phoneNum.text = rehabCenter.phone_num
//    }
//
//    override fun getItemCount(): Int {
//        return tempRehabList.size
//    }
//}