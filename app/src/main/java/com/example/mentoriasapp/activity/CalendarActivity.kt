package com.example.mentoriasapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.example.mentoriasapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {
    private lateinit var calendarView: CalendarView
    private var books: MutableMap<String, String> = mutableMapOf()
    public var bookDay = ""
    public var bookMonth = ""
    public var bookYear = ""
    public var bookMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configurar la localización a español
        val locale = Locale("es", "ES")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)

        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        val calendars: ArrayList<CalendarDay> = ArrayList()
        val calendar = Calendar.getInstance()

        //Boton para acceder al home
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        homeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Boton para acceder al perfil
        val profile_button: LinearLayout = findViewById(R.id.profile_button)
        profile_button.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // VARIABLE PARA LAS RESERVAS
        searchCurrentUserBookings { listOfBookings ->
            if (listOfBookings != null) {
                val calendars = mutableListOf<CalendarDay>()

                for (bookings in listOfBookings.children) {
                    bookMessage = "Mentoría reservada con " + bookings.key.toString() + "."
                    val bookingDate = bookings.value

                    bookDay = bookingDate.toString().split("/")[0]
                    bookMonth = bookingDate.toString().split("/")[1]
                    bookYear = bookingDate.toString().split("/")[2]

                    val calendar = Calendar.getInstance()
                    calendar.set(bookYear.toInt(), bookMonth.toInt() - 1, bookDay.toInt())

                    val calendarDay = CalendarDay(calendar)
                    calendarDay.labelColor = R.color.yellow
                    calendarDay.imageResource = R.drawable.baseline_bookmarks_24

                    calendars.add(calendarDay)

                    books["$bookDay-$bookMonth-$bookYear"] = bookMessage
                }
                calendarView.setCalendarDays(calendars)
            }
        }

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val day = String.format("%02d", calendarDay.calendar.get(Calendar.DAY_OF_MONTH))
                val month = String.format("%02d", calendarDay.calendar.get(Calendar.MONTH) + 1)
                val year = calendarDay.calendar.get(Calendar.YEAR)

                if (books.containsKey("$day-$month-$year")) {
                    Toast.makeText(baseContext, books["$day-$month-$year"], Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "No hay ninguna mentoría reservada.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                val month = String.format("%02d", calendarView.currentPageDate.get(Calendar.MONTH) + 1)
                val year = calendarView.currentPageDate.get(Calendar.YEAR)

                Toast.makeText(baseContext, "$month-$year", Toast.LENGTH_SHORT).show()
            }
        })

        calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                val month = String.format("%02d", calendarView.currentPageDate.get(Calendar.MONTH) + 1)
                val year = calendarView.currentPageDate.get(Calendar.YEAR)

                Toast.makeText(baseContext, "$month-$year", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchCurrentUserBookings (callback: (DataSnapshot) -> Unit){
        val emailOfUser = FirebaseAuth.getInstance().currentUser?.email.toString()
        val dataBaseReference = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app/")
        val userList = dataBaseReference.getReference("alumnos")

        userList.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").value.toString()
                    if (email == emailOfUser) {
                        val targetBooking = userSnapshot.child("bookings")
                        callback(targetBooking)
                        return@addOnSuccessListener
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseError", "Error al obtener los alumnos", exception)
        }
    }
}
