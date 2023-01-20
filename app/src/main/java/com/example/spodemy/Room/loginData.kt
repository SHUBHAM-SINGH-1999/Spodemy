package com.example.spodemy.Room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "userData")
data class RegisterData(
    @PrimaryKey(autoGenerate = false)
    val Email:String,
    val Password:String,
    val firstName:String,
    val surname:String,
    val Date:String,
    val ContactNumber:String,
    val gender:String,
    val age:String,
    val NumberOfGlasses: Int
    ):Parcelable


