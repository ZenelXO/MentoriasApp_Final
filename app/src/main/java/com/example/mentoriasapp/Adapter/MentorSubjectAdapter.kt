package com.example.mentoriasapp.Adapter

import android.content.Context
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
import com.example.mentoriasapp.databinding.ViewHolderDetailSubjectBinding
import com.example.mentoriasapp.databinding.ViewholderSubjectBinding

class MentorSubjectAdapter(val items:ArrayList<String>): RecyclerView.Adapter<MentorSubjectAdapter.Viewholder>(){
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context:Context
    
    class Viewholder(val binding:ViewHolderDetailSubjectBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorSubjectAdapter.Viewholder {
        context=parent.context
        val binding=ViewHolderDetailSubjectBinding.inflate(LayoutInflater.from(context), parent, false)

        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: MentorSubjectAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.subjectDetailText.text = item
        Log.i("penepolla", "$item")

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        if(selectedPosition == position){
            holder.binding.subjectDetailText.setTextColor(context.resources.getColor(R.color.white))
            holder.binding.subjectDetailLayout.setBackgroundResource(R.drawable.blue_bg)
        }else{
            holder.binding.subjectDetailLayout.setBackgroundResource(0)
            holder.binding.subjectDetailText.setTextColor(context.resources.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int = items.size
}