package com.example.mentoriasapp.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mentoriasapp.Model.SubjectModel
import com.example.mentoriasapp.R
import com.example.mentoriasapp.databinding.ViewholderSubjectBinding

class SubjectAdapter(val items:MutableList<SubjectModel>): RecyclerView.Adapter<SubjectAdapter.Viewholder>(){
    private var selectedPosition = -1
    private var lastSelectedPosition = -1
    private lateinit var context:Context

    class Viewholder(val binding:ViewholderSubjectBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderSubjectBinding.inflate(LayoutInflater.from(context), parent, false)

        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: SubjectAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.subjectName.text = item.title

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.subjectPic)

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        holder.binding.subjectName.setTextColor(context.resources.getColor(R.color.white))
        if(selectedPosition == position){
            holder.binding.subjectPic.setBackgroundResource(0)
            holder.binding.mainLayout.setBackgroundResource(R.drawable.blue_bg)

            holder.binding.subjectName.visibility = View.VISIBLE
        }else{
            holder.binding.subjectPic.setBackgroundResource(R.drawable.grey_background)
            holder.binding.mainLayout.setBackgroundResource(0)

            holder.binding.subjectName.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}