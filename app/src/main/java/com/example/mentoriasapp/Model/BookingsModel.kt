package com.example.mentoriasapp.Model

import android.os.Parcel
import android.os.Parcelable

data class BookingsModel(
    var date : ArrayList<String> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel):this(
        parcel.createStringArrayList() as ArrayList<String>
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringList(date)

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
