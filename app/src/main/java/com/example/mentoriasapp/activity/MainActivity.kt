package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentoriasapp.Adapter.MentorAdapter
import com.example.mentoriasapp.Adapter.SubjectAdapter
import com.example.mentoriasapp.R
import com.example.mentoriasapp.ViewModel.MainViewModel
import com.example.mentoriasapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Aquí cambiamos el nombre del usuario
        val user = FirebaseAuth.getInstance().currentUser?.email
        val textView: TextView = findViewById(R.id.textView5)
        textView.text = user.toString()

        //Boton para acceder al perfil
        val profileButton: LinearLayout = findViewById(R.id.profile_button)
        profileButton.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        initSubject()
        initMentor()
    }

    private fun initSubject(){
        binding.progressBarSubjects.visibility = View.VISIBLE
        viewModel.subjects.observe(this, Observer {binding.viewSubjects.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewSubjects.adapter = SubjectAdapter(it)
            binding.progressBarSubjects.visibility = View.GONE
        })

        viewModel.loadSubjects()
    }

    private fun initMentor(){
        binding.progressBarMentores.visibility = View.VISIBLE
        viewModel.mentores.observe(this, Observer {binding.viewMentores.layoutManager =
            GridLayoutManager(this@MainActivity, 2)
            binding.viewMentores.adapter = MentorAdapter(it)
            binding.progressBarMentores.visibility = View.GONE
        })

        viewModel.loadMentores()
    }
}