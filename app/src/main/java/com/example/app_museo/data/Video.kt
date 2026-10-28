package com.example.app_museo.data

import com.example.app_museo.R

data class Video(
    val titulo: String,
    val descripcion: String,
    val url: String,
    val imagenResId: Int = R.drawable.cat1
)
