package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mentoriasapp.Adapter.MentorAdapter
import com.example.mentoriasapp.Adapter.MentorSubjectAdapter
import com.example.mentoriasapp.Adapter.SubjectAdapter
import com.example.mentoriasapp.Model.ItemModel
import com.example.mentoriasapp.R
import com.example.mentoriasapp.databinding.ActivityMentorDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MentorDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityMentorDetailsBinding
    private lateinit var item:ItemModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el objeto item del Intent
        item = intent.getParcelableExtra("object") ?: throw IllegalArgumentException("Item no encontrado en el Intent")

        // Botón para volver
        val back_button: ImageView = findViewById(R.id.backButton)
        back_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Imagen de mentor
        val mentor_image : ImageView = findViewById(R.id.mentor_pic_container)
        Glide.with(this)
            .load(item.picUrl)
            .into(mentor_image)

        //Descripción del mentor
        val mentor_description : TextView = findViewById(R.id.mentor_description)
        mentor_description.text = item.description

        // Inicializar la lista de materias
        initSubjectsList()
    }

    private fun initSubjectsList() {
        val subjectList = ArrayList<String>()
        // Verificar si la lista de datos existe y tiene elementos
        if (::item.isInitialized) {
            for (subject in item.mentor_subjects) {
                subjectList.add(subject)
            }

            binding.subjectList.adapter= MentorSubjectAdapter(subjectList)
            binding.subjectList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

    }
}
