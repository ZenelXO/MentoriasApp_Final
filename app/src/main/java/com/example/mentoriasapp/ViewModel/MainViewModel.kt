package com.example.mentoriasapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mentoriasapp.Model.ItemModel
import com.example.mentoriasapp.Model.SubjectModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Ref

class MainViewModel {
    private val firebaseDatabase = FirebaseDatabase.getInstance("https://mentoriasapp-default-rtdb.europe-west1.firebasedatabase.app")
    private val _mentor = MutableLiveData<MutableList<ItemModel>>()
    val mentores: LiveData<MutableList<ItemModel>> = _mentor

    private val _subject = MutableLiveData<MutableList<SubjectModel>>()
    val subjects: LiveData<MutableList<SubjectModel>> = _subject

    fun loadSubjects() {
        val Ref = firebaseDatabase.getReference("subjects")
        Ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SubjectModel>()
                for(childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SubjectModel::class.java)
                    if(list != null){
                        lists.add(list)
                    }
                }
                _subject.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun loadMentores() {
        val Ref = firebaseDatabase.getReference("mentores")
        Ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemModel>()
                for(childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(ItemModel::class.java)
                    if(list != null){
                        lists.add(list)
                    }
                }
                _mentor.value = lists
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}