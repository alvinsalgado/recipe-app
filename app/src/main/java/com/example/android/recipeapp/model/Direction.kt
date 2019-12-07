package com.example.android.recipeapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Direction(val directions: String
) : Parcelable {constructor() : this ("")}