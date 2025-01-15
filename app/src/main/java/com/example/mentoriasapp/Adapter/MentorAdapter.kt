package com.example.mentoriasapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mentoriasapp.Model.ItemModel
import com.example.mentoriasapp.databinding.ActivityMainBinding
import com.example.mentoriasapp.databinding.ViewholderMentoresBinding

class MentorAdapter(val items:MutableList<ItemModel>):RecyclerView.Adapter<MentorAdapter.ViewHolder>() {
    private var context: Context ?= null

    class ViewHolder(val binding: ViewholderMentoresBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderMentoresBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MentorAdapter.ViewHolder, position: Int) {
        holder.binding.nameMentor.text = items[position].name
        holder.binding.subjectMentor.text = items[position].mentor_subjects.toString()
        holder.binding.rating.text = items[position].rating.toString()

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].picUrl)
            .apply(requestOptions)
            .into(holder.binding.subjectPic)

        //AQUI ES PARA IR A CADA MENTOR
        //holder.itemView.setOnClickListener{
            //val intent = Intent(holder.itemView.context, )
        //}
    }

    override fun getItemCount(): Int = items.size
}