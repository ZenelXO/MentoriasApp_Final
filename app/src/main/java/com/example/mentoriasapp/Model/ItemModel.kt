package com.example.mentoriasapp.Model

import android.os.Parcel
import android.os.Parcelable

data class ItemModel(
    var name: String = "",
    var description: String = "",
    var mentor_subjects: ArrayList<String> = ArrayList(),
    var picUrl: String = "",
    var rating: Double = 0.0
): Parcelable{
    constructor(parcel: Parcel):this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readString().toString(),
        parcel.readDouble()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(description)
        dest.writeStringList(mentor_subjects)
        dest.writeString(picUrl)
        dest.writeDouble(rating)
    }

    companion object CREATOR : Parcelable.Creator<ItemModel> {
        override fun createFromParcel(parcel: Parcel): ItemModel {
            return ItemModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemModel?> {
            return arrayOfNulls(size)
        }
    }
}