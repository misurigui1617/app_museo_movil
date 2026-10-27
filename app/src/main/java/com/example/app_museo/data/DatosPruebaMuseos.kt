package com.example.app_museo.data

import com.example.app_museo.Lugar
import org.osmdroid.util.GeoPoint

object DatosPruebaMuseos {

    val listaMuseos = listOf(
        Lugar(
            id = 1,
            nombre = "Museo Nacional de Bellas Artes",
            direccion = "José Miguel de La Barra 650, Santiago",
            pais = "Chile",
            categoria = "Proyecto",
            punto = GeoPoint(-33.4353, -70.6433),
            valoracion = 4.8,
            descripcion = "Principal centro de difusión de las artes visuales en Chile."
        ),
        Lugar(
            id = 2,
            nombre = "Museo Histórico Nacional",
            direccion = "Plaza de Armas 951, Santiago",
            pais = "Chile",
            categoria = "País",
            punto = GeoPoint(-33.4376, -70.6506),
            valoracion = 4.7,
            descripcion = "Patrimonio histórico que rescata la memoria e identidad de Chile."
        ),
        Lugar(
            id = 3,
            nombre = "Museo Chileno de Arte Precolombino",
            direccion = "Bandera 361, Santiago",
            pais = "Chile",
            categoria = "Proyecto",
            punto = GeoPoint(-33.4389, -70.6528),
            valoracion = 4.9,
            descripcion = "Excepcional colección de arte prehispánico de la región."
        ),
        Lugar(
            id = 4,
            nombre = "Museo del Oro",
            direccion = "Cra. 6 #15-88, Bogotá",
            pais = "Colombia",
            categoria = "País",
            punto = GeoPoint(4.6017, -74.0721),
            valoracion = 4.9,
            descripcion = "Colección de orfebrería prehispánica más importante del mundo."
        ),
        Lugar(
            id = 5,
            nombre = "Museo Nacional de Antropología",
            direccion = "Av. P.º de la Reforma s/n, Ciudad de México",
            pais = "México",
            categoria = "País",
            punto = GeoPoint(19.4260, -99.1863),
            valoracion = 5.0,
            descripcion = "Icono de la arquitectura y la arqueología mesoamericana."
        ),
        Lugar(
            id = 6,
            nombre = "Museo de Arte de Lima (MALI)",
            direccion = "Paseo Colón 125, Lima",
            pais = "Perú",
            categoria = "Proyecto",
            punto = GeoPoint(-12.0601, -77.0366),
            valoracion = 4.7,
            descripcion = "3,000 años de arte peruano conservados en el Palacio de la Exposición."
        ),
        Lugar(
            id = 7,
            nombre = "Museo del Prado",
            direccion = "Calle de Ruiz de Alarcón 23, Madrid",
            pais = "España",
            categoria = "Proyecto",
            punto = GeoPoint(40.4138, -3.6921),
            valoracion = 4.9,
            descripcion = "Referente mundial en pintura española y europea."
        )
    )

    val categoriasFiltro = listOf("Todos", "País", "Proyecto", "Chile", "Colombia", "México", "Perú", "España")
}
