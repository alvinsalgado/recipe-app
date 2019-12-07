package com.example.android.recipeapp.common

import android.view.View

inline fun View.onClick(crossinline onClickHandler: () -> Unit) {
    setOnClickListener { onClickHandler() }
}