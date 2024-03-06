package com.dicoding.proyekakhirbeginnerandroid

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tour(
    val title: String,
    val description: String,
    val location: String,
    val rating: String,
    val image: String
) : Parcelable
