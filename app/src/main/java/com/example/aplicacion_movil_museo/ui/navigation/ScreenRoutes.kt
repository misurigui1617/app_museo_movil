package com.example.aplicacion_movil_museo.ui.navigation

import android.net.Uri

sealed class Screen(val route: String, val title: String) {
    object Bienvenida : Screen("bienvenida", "Bienvenida")
    object InicioSesion : Screen("inicio_sesion", "Inicio de Sesión")
    object Registro : Screen("registro", "Registro")
    object Principal : Screen("principal", "Inicio")
    object Mapa : Screen("mapa", "Mapa Museos")
    object DetalleMuseo : Screen("detalle_museo/{museumId}", "Detalle Museo") {
        fun crearRuta(museumId: String): String {
            val safeId = Uri.encode(museumId)
            return "detalle_museo/$safeId"
        }
    }
    object EscanerQr : Screen("escaner_qr", "Escáner QR")
    object ResultadoQr : Screen("resultado_qr/{code}", "Resultado QR") {
        fun crearRuta(code: String): String {
            val safeCode = Uri.encode(code)
            return "resultado_qr/$safeCode"
        }
    }
    object Catalogo : Screen("catalogo", "Catálogo")
    object DetalleProducto : Screen("detalle_producto/{productId}", "Detalle Producto") {
        fun crearRuta(productId: String): String {
            val safeId = Uri.encode(productId)
            return "detalle_producto/$safeId"
        }
    }
    object Favoritos : Screen("favoritos", "Favoritos")
    object Carrito : Screen("carrito", "Carrito")
    object HistorialSolicitudes : Screen("historial_solicitudes", "Historial")
    object Notificaciones : Screen("notificaciones", "Notificaciones")
    object Perfil : Screen("perfil", "Perfil")
}
