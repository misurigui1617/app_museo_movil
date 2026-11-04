package com.example.aplicacion_movil_museo.data.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String = "demo",
    val role: UserRole,
    val avatarUrl: String = "",
    val preferences: Map<String, String> = emptyMap()
)

data class Museum(
    val id: String,
    val name: String,
    val country: String,
    val city: String,
    val description: String,
    val classification: String,
    val virtualTourUrl: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false
)

enum class ItemType { PRODUCT, COURSE }

data class ProductCourse(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val description: String,
    val type: ItemType,
    val isAvailable: Boolean = true,
    val imageUrl: String = ""
)

data class CartItem(
    val productCourse: ProductCourse,
    val quantity: Int
)

enum class RequestStatus(val label: String) {
    PENDING("Pendiente"),
    CONFIRMED("Confirmado"),
    SHIPPED("Enviado"),
    DELIVERED("Entregado")
}

data class RequestOrder(
    val id: String,
    val userId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val shippingCost: Double,
    val isInternational: Boolean,
    val shippingAddress: String,
    val date: String,
    val status: RequestStatus = RequestStatus.PENDING
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val targetScreenRoute: String? = null,
    val isRead: Boolean = false
)

data class QrRecord(
    val id: String,
    val code: String,
    val title: String,
    val type: String, // Museo, Producto, Curso
    val targetId: String,
    val timestamp: String
)
