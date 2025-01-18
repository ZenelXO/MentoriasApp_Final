package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values

class MainActivity : BaseActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    private var selectedSubject: String = "Todo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Aquí cambiamos el nombre del usuario
        val textView: TextView = findViewById(R.id.textView5)
        searchUser { targetUser ->
            if (targetUser.isNotEmpty()) {
                textView.text = targetUser
            }
        }

        //Boton para acceder al perfil
        val profileButton: LinearLayout = findViewById(R.id.profile_button)
        profileButton.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        //Boton para acceder al calendario
        val calendarButton: LinearLayout = findViewById(R.id.calendarButton)
        calendarButton.setOnClickListener{
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        initMentor()

        initSubject() { selectedSubjectName ->
            selectedSubject = selectedSubjectName
            Log.d("SelectedSubject", "Asignatura seleccionada: $selectedSubject")

            initMentor()
        }

    }

    private fun searchUser(callback: (String) -> Unit) {
        val emailOfUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
        val userList = dataBaseReference.getReference("alumnos")

        userList.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").value.toString()
                    if (email == emailOfUser) {
                        val targetUser = userSnapshot.child("name").value.toString()
                        callback(targetUser)
                        return@addOnSuccessListener
                    }
                }
            }
            callback("")
        }.addOnFailureListener { exception ->
            Log.e("FirebaseError", "Error al obtener los alumnos", exception)
            callback("")
        }
    }

    private fun initSubject(callback: (String) -> Unit) {
        binding.progressBarSubjects.visibility = View.VISIBLE

        viewModel.subjects.observe(this, Observer { subjectList ->
            val filteredSubjects = subjectList.drop(1).toMutableList()

            binding.viewSubjects.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewSubjects.adapter = SubjectAdapter(filteredSubjects) { selectedSubjectName ->
                selectedSubject = selectedSubjectName
                Log.d("SelectedSubject", "Asignatura seleccionada: $selectedSubject")

                callback(selectedSubject)
            }

            binding.progressBarSubjects.visibility = View.GONE
        })

        viewModel.loadSubjects()
    }



    private fun initMentor() {
        binding.progressBarMentores.visibility = View.VISIBLE

        viewModel.mentores.observe(this, Observer { mentorList ->

            val filteredMentors = if (selectedSubject == "Todo") {
                mentorList.toMutableList()
            } else {
                mentorList.filter { mentor ->
                    mentor.mentor_subjects.contains(selectedSubject)
                }.toMutableList()
            }

            binding.viewMentores.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewMentores.adapter = MentorAdapter(filteredMentors, selectedSubject)

            binding.progressBarMentores.visibility = View.GONE
        })

        viewModel.loadMentores()
    }


}