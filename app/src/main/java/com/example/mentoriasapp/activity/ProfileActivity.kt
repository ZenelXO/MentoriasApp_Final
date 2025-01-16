package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mentoriasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val home_button: LinearLayout = findViewById(R.id.home_button)
        home_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Aquí cambiamos el nombre del usuario
        val textViewName: TextView = findViewById(R.id.userName)
        searchUserName { targetUser ->
            if (targetUser.isNotEmpty()) {
                textViewName.text = targetUser
            }
        }
        //Aquí cambiamos la biografia del usuario
        val textViewBiography: TextView = findViewById(R.id.userBiography)
        searchUserBiography { targetUser ->
            if (targetUser.isNotEmpty()) {
                textViewBiography.text = targetUser
            }
        }
        //Aquí cambiamos la foto del usuario
        val textViewPic: ImageView = findViewById(R.id.mentor_pic_container)
        searchUserPic { targetUser ->
            if (targetUser.isNotEmpty()) {
                Glide.with(this)
                    .load(targetUser)
                    .into(textViewPic)
            }else{
                Glide.with(this)
                    .load("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ficon-library.com%2Fimages%2Fpersona-icon%2Fpersona-icon-26.jpg&f=1&nofb=1&ipt=6affc600830d25b467b99b595fc98096f80381d35af20a0fda8756171f6e9b04&ipo=images")
                    .into(textViewPic)
            }
        }
    }

    private fun searchUserName(callback: (String) -> Unit) {
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

    private fun searchUserBiography(callback: (String) -> Unit) {
        val emailOfUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
        val userList = dataBaseReference.getReference("alumnos")

        userList.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").value.toString()
                    if (email == emailOfUser) {
                        val targetUser = userSnapshot.child("biografia").value.toString()
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

    private fun searchUserPic(callback: (String) -> Unit) {
        val emailOfUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
        val userList = dataBaseReference.getReference("alumnos")

        userList.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").value.toString()
                    if (email == emailOfUser) {
                        val targetUser = userSnapshot.child("picUrl").value.toString()
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