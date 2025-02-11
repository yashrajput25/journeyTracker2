package com.example.journeytracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class StopsAdapter(private val stops: List<String>, private val currentStop: Int): RecyclerView.Adapter<StopsAdapter.StopViewHolder>(){

    class StopViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvStopName: TextView = itemView.findViewById((R.id.tvStopName))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stop, parent, false)
        return StopViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        holder.tvStopName.text = stops[position]

        if(position == currentStop-1){
            holder.tvStopName.setTextColor(Color.RED)
        }
        else{
            holder.tvStopName.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return stops.size
    }


}