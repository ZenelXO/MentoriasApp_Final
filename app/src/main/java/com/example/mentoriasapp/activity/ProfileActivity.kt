package com.example.mentoriasapp.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mentoriasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ProfileActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    lateinit var encodedImage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //Aqui configuramos el funcionamiento de los botones del Perfil/NavigationBar
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
        val editProfileButton: Button = findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener{
            updateUserInfo { result ->
                if (result == "Actualización exitosa") {
                    recreate()
                }
            }
        }
        imageView = findViewById(R.id.mentor_pic_container)
        val changePicButton: Button = findViewById(R.id.changePicButton)
        changePicButton.setOnClickListener{
            openGallery()
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
        val progressBar: ProgressBar = findViewById(R.id.progressBarProfile)

        // Mostrar el ProgressBar inicialmente
        progressBar.visibility = View.VISIBLE

        searchUserPic { targetUser ->
            if (targetUser.isNotEmpty()) {
                // Verificar si es una imagen en Base64 o una URL
                if (targetUser.startsWith("data:image") || isBase64(targetUser)) {
                    // Es Base64: Decodificar y mostrar
                    val decodedBytes = Base64.decode(targetUser.substringAfter(","), Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    textViewPic.setImageBitmap(bitmap)
                    progressBar.visibility = View.GONE // Ocultar el ProgressBar al finalizar
                } else {
                    // Es una URL: Cargar usando Glide
                    Glide.with(this)
                        .load(targetUser)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = View.GONE // Ocultar si falla
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = View.GONE // Ocultar al cargar con éxito
                                return false
                            }
                        })
                        .into(textViewPic)
                }
            } else {
                Glide.with(this)
                    .load("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ficon-library.com%2Fimages%2Fpersona-icon%2Fpersona-icon-26.jpg&f=1&nofb=1&ipt=6affc600830d25b467b99b595fc98096f80381d35af20a0fda8756171f6e9b04&ipo=images")
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE // Ocultar si falla
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE // Ocultar al cargar con éxito
                            return false
                        }
                    })
                    .into(textViewPic)
            }
        }
    }

    private fun isBase64(data: String): Boolean {
        return try {
            Base64.decode(data, Base64.DEFAULT)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val imageUri = data?.data
            if (imageUri != null) {
                imageView.setImageURI(imageUri)

                val base64Image = encodeImageToBase64(imageUri)
                if (base64Image != null) {
                    encodedImage = base64Image
                    updateUserImage { result ->
                        if (result == "Actualización exitosa") {
                            recreate()
                        }
                    }
                    Log.i("Base64Image", base64Image)
                }
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun encodeImageToBase64(imageUri: Uri): String? {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun updateUserImage(callback: (String) -> Unit) {
        searchUserKey { currentUserId ->
            if (currentUserId.isNotEmpty()) {
                val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
                val userList = dataBaseReference.getReference("alumnos")

                val userReference = userList.child(currentUserId)
                userReference.child("picUrl").setValue(encodedImage)
                    .addOnCompleteListener {
                        callback("Actualización exitosa")
                    }
            } else {
                callback("Error: No se encontró el usuario")
            }
        }
    }

    private fun updateUserInfo(callback: (String) -> Unit) {
        searchUserKey { currentUserId ->
            if (currentUserId.isNotEmpty()) {
                val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
                val userList = dataBaseReference.getReference("alumnos")
                val changedName = findViewById<EditText>(R.id.userName).text.toString()
                val changedBiography = findViewById<EditText>(R.id.userBiography).text.toString()

                val userReference = userList.child(currentUserId)
                userReference.child("name").setValue(changedName)
                userReference.child("biografia").setValue(changedBiography)
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