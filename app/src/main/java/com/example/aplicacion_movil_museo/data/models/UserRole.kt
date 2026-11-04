package com.example.aplicacion_movil_museo.data.models

enum class UserRole(val label: String, val description: String) {
    ADMIN("Administrador", "Gestión de productos, cursos, solicitudes y contenidos"),
    INVESTIGATOR("Investigador", "Registro y revisión de museos virtuales, QR y geolocalización"),
    CLIENT("Cliente", "Consulta de museos, carrito, solicitudes, favoritos y notificaciones")
}
