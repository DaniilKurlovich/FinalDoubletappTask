package com.example.finaltaskandroid.data.models

import android.os.Parcel
import android.os.Parcelable

data class HabitUID(val uid: String)
data class HabitDone(val uid: String, val date: Int)

data class Habit(val title: String, val description: String, val priority: Int, val type: Int,
                 val frequency: Int, var count: Int, var uid: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeInt(type)
        parcel.writeInt(frequency)
        parcel.writeInt(count)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}