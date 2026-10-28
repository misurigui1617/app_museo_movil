package com.example.app_museo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioEmail: String,
    val nombreMuseo: String,
    val pais: String,
    val tipo: String,
    val imagenResId: Int
)