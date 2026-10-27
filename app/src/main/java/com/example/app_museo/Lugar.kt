package com.example.app_museo

import org.osmdroid.util.GeoPoint

data class Lugar(
    val id: Int = 0,
    val nombre: String,
    val direccion: String,
    val pais: String = "Chile",
    val categoria: String = "Proyecto",
    val punto: GeoPoint,
    val valoracion: Double = 4.8,
    val descripcion: String = "Patrimonio histórico y cultural destacado.",
    val imagenUrl: String = ""
)
