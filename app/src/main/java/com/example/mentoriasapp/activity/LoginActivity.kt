package com.example.mentoriasapp.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.mentoriasapp.R
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.log

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val editTextEmail: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editTextPassword: EditText = findViewById(R.id.editTextTextPassword)
        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Por favor introduzca usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Por favor introduzca la contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }
}