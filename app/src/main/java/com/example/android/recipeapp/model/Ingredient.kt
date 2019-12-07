package com.example.android.recipeapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient(val ingredient: String
) : Parcelable {constructor() : this ("")}