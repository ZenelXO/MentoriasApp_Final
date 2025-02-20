package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mentoriasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val editTextName: EditText = findViewById(R.id.editTextNameContainer)
        val editTextBiography: EditText = findViewById(R.id.editTextBiography)
        val editTextEmail: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editTextEmail2: EditText = findViewById(R.id.editTextTextEmailAddress2)
        val editTextPassword: EditText = findViewById(R.id.editTextTextPassword)
        val editTextPassword2: EditText = findViewById(R.id.editTextTextPassword2)
        val loginButton: Button = findViewById(R.id.login_button)
        val auth:FirebaseAuth = FirebaseAuth.getInstance()
        val dataBaseRealTime:FirebaseDatabase = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
        val dataBaseReference = dataBaseRealTime.reference

        loginButton.setOnClickListener {
            var nameStudent = editTextName.text.toString()
            var biographyStudent = editTextBiography.text.toString()

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            val email2 = editTextEmail2.text.toString()
            val password2 = editTextPassword2.text.toString()

            //Comprobamos si no esta vacio
            if (nameStudent.isEmpty()){
                nameStudent = "Estudiante_Anonimo"
            }

            if (biographyStudent.isEmpty()){
                biographyStudent = "Biografía del estudiante."
            }

            if (email.isEmpty() and email2.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Por favor introduzca usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty() and password2.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Por favor introduzca la contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Comprobamos si los campos son iguales
            if (email != email2) {
                Toast.makeText(this@RegisterActivity, "Los usuarios no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != password2) {
                Toast.makeText(this@RegisterActivity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Aqui es donde creamos la cuenta en FIREBASE-----------------------------------------
            val userId = dataBaseReference.push().key
            val userInfo = mapOf(
                "email" to email,
                "name" to nameStudent,
                "biografia" to biographyStudent,
                "picUrl" to "",
                "reservas" to listOf<Map<String, Any>>()
            )
            try {
                dataBaseReference.child("alumnos").child(userId!!).setValue(userInfo)
            } catch (e: DatabaseException) {
                Log.e("FirebaseError", "Error writing to database: ${e.message}")
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Account created sucessfully",
                            Toast.LENGTH_SHORT,
                        ).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}