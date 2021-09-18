package com.proactiveants.mytrackergateway

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataSet: ArrayList<User>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewTel: TextView = view.findViewById(R.id.textViewTel)
        val imageViewPhoto: ImageView = view.findViewById(R.id.imageViewPhoto)
        val textViewAddress: TextView = view.findViewById(R.id.textViewAddress)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewName.text = dataSet[position].name
        viewHolder.textViewTel.text = dataSet[position].telephone
        viewHolder.imageViewPhoto.setImageResource(R.drawable.kit)
        viewHolder.textViewAddress.text = dataSet[position].address
        viewHolder.textViewDate.text = dataSet[position].date
    }

    override fun getItemCount() = dataSet.size

}