package com.example.mentoriasapp.activity

import android.app.DatePickerDialog
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
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.util.Calendar
import java.util.Locale

class MentorDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityMentorDetailsBinding
    private lateinit var item:ItemModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentorDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Poner el idioma en español
        val locale = Locale("es", "ES")
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)

        // Recuperar el objeto item del Intent
        item = intent.getParcelableExtra("object") ?: throw IllegalArgumentException("Item no encontrado en el Intent")

        // Botón para volver
        val back_button: ImageView = findViewById(R.id.backButton)
        back_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón para reservar
        val book_mentor_button: AppCompatButton = findViewById(R.id.book_mentor_button)
        book_mentor_button.setOnClickListener {
            showDatePicker { selectedDate ->
                addUserBookings(selectedDate) { result ->
                    if (result == "Actualización exitosa") {
                        Log.i("Reserva", "Reservado")
                    }
                }
            }
        }

        //Caracteristicas del mentor
        val mentor_name: TextView = findViewById(R.id.mentor_name_detail)
        mentor_name.text = item.name

        val mentor_description : TextView = findViewById(R.id.mentor_description)
        mentor_description.text = item.description
        
        val mentor_rating: TextView = findViewById(R.id.mentor_rating_detail)
        mentor_rating.text = item.rating.toString()

        val mentor_image : ImageView = findViewById(R.id.mentor_pic_container)
        Glide.with(this)
            .load(item.picUrl)
            .into(mentor_image)

        // Inicializar la lista de materias
        initSubjectsList()
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear el DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                onDateSelected(formattedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        // Mostrar el DatePickerDialog
        datePickerDialog.show()
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

    private fun addUserBookings(bookingDate: String, callback: (String) -> Unit) {
        searchUserKey { currentUserId ->
            if (currentUserId.isNotEmpty()) {
                val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
                val userList = dataBaseReference.getReference("alumnos")

                val userReference = userList.child(currentUserId)
                userReference.child("bookings").child(item.name).setValue(bookingDate)
                    .addOnCompleteListener {
                        callback("Actualización exitosa")
                    }
            } else {
                callback("Error: No se encontró el usuario")
            }
        }
    }

    private fun searchUserKey(callback: (String) -> Unit) {
        val emailOfUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
        val userList = dataBaseReference.getReference("alumnos")

        userList.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").value.toString()
                    if (email == emailOfUser) {
                        val targetUser = userSnapshot.key.toString()
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
}
