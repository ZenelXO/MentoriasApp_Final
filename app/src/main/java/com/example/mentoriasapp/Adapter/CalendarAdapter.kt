package com.example.mentoriasapp.Adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mentoriasapp.R
import com.example.mentoriasapp.activity.CalendarActivity
import com.example.mentoriasapp.activity.MentorDetailsActivity
import com.example.mentoriasapp.databinding.ViewHolderDetailSubjectBinding
import com.example.mentoriasapp.databinding.ViewholderCalendarBinding
import com.example.mentoriasapp.databinding.ViewholderSubjectBinding

class CalendarAdapter(val items:ArrayList<String>): RecyclerView.Adapter<CalendarAdapter.Viewholder>(){
    private lateinit var context:Context
    
    class Viewholder(val binding:ViewholderCalendarBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderCalendarBinding.inflate(LayoutInflater.from(context), parent, false)

        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CalendarAdapter.Viewholder, position: Int) {
        holder.binding.calendarText.text = items[position]
        val hola = items[position]
        Log.i("buenas", "$hola")
    }

    override fun getItemCount(): Int = items.size
}