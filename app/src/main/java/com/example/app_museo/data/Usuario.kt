package com.example.app_museo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val tipoCliente: String = "Cliente",
    val fotoUri: String? = ""
)
