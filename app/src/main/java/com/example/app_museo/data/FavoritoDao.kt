package com.example.app_museo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritoDao {
    @Query("SELECT * FROM favoritos WHERE usuarioEmail = :email")
    suspend fun obtenerFavoritosPorUsuario(email: String): List<FavoritoEntity>

    @Query("SELECT * FROM favoritos WHERE usuarioEmail = :email AND tipo = :tipo")
    suspend fun obtenerFavoritosPorTipo(email: String, tipo: String): List<FavoritoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarFavorito(favorito: FavoritoEntity)

    @Delete
    suspend fun eliminarFavorito(favorito: FavoritoEntity)
}