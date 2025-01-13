package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.mentoriasapp.MainActivity
import com.example.mentoriasapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.math.log

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val editTextEmail: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editTextPassword: EditText = findViewById(R.id.editTextTextPassword)
        val loginButton: Button = findViewById(R.id.login_button)
        lateinit var auth: FirebaseAuth
        auth = Firebase.auth

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            //Comprobamos si esta vacio
            if (email.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Por favor introduzca usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Por favor introduzca la contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Inicio de sesión satisfactorio!",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val user = auth.currentUser

                        //Se redirige al MainActivity---------
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Inicio de sesión fallido!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}