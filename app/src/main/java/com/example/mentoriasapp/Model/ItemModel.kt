package com.example.mentoriasapp.Model

data class ItemModel(
    var name: String = "",
    var description: String = "",
    var mentor_subjects: ArrayList<String> = ArrayList(),
    var picUrl: String = "",
    var rating: Double = 0.0
)