package com.example.app_museo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Insert
    suspend fun registrarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND contrasena = :contrasena LIMIT 1")
    suspend fun login(email: String, contrasena: String): Usuario?
}
