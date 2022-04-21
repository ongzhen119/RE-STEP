package com.example.ohxinli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TreatmentAdapter(private var treatmentList: ArrayList<Treatment2>) : RecyclerView.Adapter<TreatmentAdapter.ViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    var tempTreatmentList = ArrayList<Treatment2>()

    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
        val treatmentName : TextView = view.findViewById(R.id.treatmentName)

        init{
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    init {
        tempTreatmentList = treatmentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.treatmentlist_item_view, parent, false)
        return ViewHolder((view), mListener as onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var treatment = treatmentList[position]
        holder.treatmentName.text = treatment.plan_name
    }

    override fun getItemCount(): Int {
        return tempTreatmentList.size
    }
}