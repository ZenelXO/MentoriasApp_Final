package com.example.mentoriasapp.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mentoriasapp.Model.ItemModel
import com.example.mentoriasapp.activity.MentorDetailsActivity
import com.example.mentoriasapp.databinding.ActivityMainBinding
import com.example.mentoriasapp.databinding.ViewholderMentoresBinding

class MentorAdapter(val items:MutableList<ItemModel>, private val selectedSubject:String):RecyclerView.Adapter<MentorAdapter.ViewHolder>() {
    private var context: Context ?= null

    class ViewHolder(val binding: ViewholderMentoresBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderMentoresBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MentorAdapter.ViewHolder, position: Int) {
        val mentor = items[position]
        val filter_mentor_subjects = mentor.mentor_subjects


        if (selectedSubject == "Todo"){
            holder.binding.nameMentor.text = mentor.name
            holder.binding.subjectMentor.text = mentor.mentor_subjects.joinToString(", ")
            holder.binding.rating.text = mentor.rating.toString()

            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(holder.itemView.context)
                .load(mentor.picUrl)
                .apply(requestOptions)
                .into(holder.binding.subjectPic)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, MentorDetailsActivity::class.java)
                intent.putExtra("object", mentor)
                holder.itemView.context.startActivity(intent)
            }

            holder.binding.nameMentor.visibility = View.VISIBLE
            holder.binding.subjectMentor.visibility = View.VISIBLE
            holder.binding.rating.visibility = View.VISIBLE
            holder.binding.subjectPic.visibility = View.VISIBLE
        }
        else if (filter_mentor_subjects.contains(selectedSubject)) {

            holder.binding.nameMentor.text = mentor.name
            holder.binding.subjectMentor.text = mentor.mentor_subjects.joinToString(", ")
            holder.binding.rating.text = mentor.rating.toString()

            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(holder.itemView.context)
                .load(mentor.picUrl)
                .apply(requestOptions)
                .into(holder.binding.subjectPic)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, MentorDetailsActivity::class.java)
                intent.putExtra("object", mentor)
                holder.itemView.context.startActivity(intent)
            }

            holder.binding.nameMentor.visibility = View.VISIBLE
            holder.binding.subjectMentor.visibility = View.VISIBLE
            holder.binding.rating.visibility = View.VISIBLE
            holder.binding.subjectPic.visibility = View.VISIBLE
        } else {
            holder.binding.nameMentor.visibility = View.GONE
            holder.binding.subjectMentor.visibility = View.GONE
            holder.binding.rating.visibility = View.GONE
            holder.binding.subjectPic.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int = items.size
}