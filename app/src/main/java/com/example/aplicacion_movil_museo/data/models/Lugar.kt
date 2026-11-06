package com.example.aplicacion_movil_museo.data.models

import org.osmdroid.util.GeoPoint

data class Lugar(
    val nombre: String,
    val direccion: String,
    val punto: GeoPoint
)
