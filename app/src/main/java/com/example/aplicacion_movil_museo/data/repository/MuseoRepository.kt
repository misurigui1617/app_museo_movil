package com.example.aplicacion_movil_museo.data.repository

import com.example.aplicacion_movil_museo.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

object MuseoRepository {

    // Default Users list with passwords
    private val defaultUsers = listOf(
        User("1", "Carlos Cliente", "cliente@museo.cl", "demo", UserRole.CLIENT, "https://picsum.photos/200"),
        User("2", "Ana Investigadora", "investigadora@museo.cl", "demo", UserRole.INVESTIGATOR, "https://picsum.photos/201"),
        User("3", "Admin Museo", "admin@museo.cl", "demo", UserRole.ADMIN, "https://picsum.photos/202")
    )

    private val _usersList = MutableStateFlow<List<User>>(defaultUsers)
    val usersList: StateFlow<List<User>> = _usersList.asStateFlow()

    private val _currentUser = MutableStateFlow<User>(defaultUsers[0])
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Museums Initial Data
    private val initialMuseums = listOf(
        Museum(
            id = "m1",
            name = "Museo del Prado Virtual",
            country = "España",
            city = "Madrid",
            description = "Explora la colección de arte renacentista y barroco más destacada de Iberoamérica y Europa.",
            classification = "Arte Clásico",
            virtualTourUrl = "https://www.museodelprado.es/recorrido-virtual",
            imageUrl = "https://images.unsplash.com/photo-1565008447742-97f6f38c985c?w=600",
            latitude = 40.4138,
            longitude = -3.6921,
            isFavorite = true
        ),
        Museum(
            id = "m2",
            name = "Museo Tamayo de Arte Contemporáneo",
            country = "México",
            city = "Ciudad de México",
            description = "Espacio dedicado a las expresiones artísticas contemporáneas y vanguardias internacionales.",
            classification = "Arte Contemporáneo",
            virtualTourUrl = "https://museotamayo.org/virtual",
            imageUrl = "https://images.unsplash.com/photo-1518998053901-5348d3961a04?w=600",
            latitude = 19.4260,
            longitude = -99.1812,
            isFavorite = false
        ),
        Museum(
            id = "m3",
            name = "Museo Nacional de Bellas Artes",
            country = "Chile",
            city = "Santiago",
            description = "Principal centro de difusión de las artes visuales en Chile con recorridos virtuales 360°.",
            classification = "Bellas Artes",
            virtualTourUrl = "https://www.mnba.gob.cl/virtual",
            imageUrl = "https://images.unsplash.com/photo-1582555172866-f73bb12a2ab3?w=600",
            latitude = -33.4358,
            longitude = -70.6436,
            isFavorite = true
        ),
        Museum(
            id = "m4",
            name = "Museo Botero",
            country = "Colombia",
            city = "Bogotá",
            description = "Colección de obras donadas por el maestro Fernando Botero e hitos del arte moderno internacional.",
            classification = "Arte Moderno",
            virtualTourUrl = "https://www.banrepcultural.org/bogota/museo-botero",
            imageUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600",
            latitude = 4.5968,
            longitude = -74.0722,
            isFavorite = false
        )
    )

    private val _museums = MutableStateFlow<List<Museum>>(initialMuseums)
    val museums: StateFlow<List<Museum>> = _museums.asStateFlow()

    // Favorites per user ID
    private val _userFavorites = MutableStateFlow<Map<String, Set<String>>>(
        mapOf("1" to setOf("m1", "m3"))
    )

    // Catalog Initial Data
    private val initialCatalog = listOf(
        ProductCourse(
            id = "p1",
            title = "Curso: Curaduría y Exposición Museográfica",
            category = "Cursos",
            price = 45000.0,
            description = "Capacitación profesional sobre diseño de exhibiciones, narrativa visual y conservación.",
            type = ItemType.COURSE,
            imageUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=600"
        ),
        ProductCourse(
            id = "p2",
            title = "Publicación: Anuario de Museos de Iberoamérica",
            category = "Publicaciones",
            price = 18500.0,
            description = "Edición impresa con análisis de más de 120 galerías y centros culturales regionales.",
            type = ItemType.PRODUCT,
            imageUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600"
        ),
        ProductCourse(
            id = "p3",
            title = "Kit de Conservación Preventiva de Papel",
            category = "Materiales",
            price = 32000.0,
            description = "Insumos especializados libre de ácido para la preservación de manuscritos e impresos.",
            type = ItemType.PRODUCT,
            imageUrl = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=600"
        ),
        ProductCourse(
            id = "p4",
            title = "Taller Virtual: Mediación Cultural y Audiencias",
            category = "Cursos",
            price = 28000.0,
            description = "Estrategias participativas para la fidelización de visitantes e integración escolar.",
            type = ItemType.COURSE,
            imageUrl = "https://images.unsplash.com/photo-1524178232363-1fb2b075b655?w=600"
        )
    )

    private val _catalog = MutableStateFlow<List<ProductCourse>>(initialCatalog)
    val catalog: StateFlow<List<ProductCourse>> = _catalog.asStateFlow()

    // Cart State
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Orders History State
    private val _orders = MutableStateFlow<List<RequestOrder>>(
        listOf(
            RequestOrder(
                id = "ORD-1001",
                userId = "1",
                items = listOf(CartItem(initialCatalog[1], 1)),
                totalAmount = 23500.0,
                shippingCost = 5000.0,
                isInternational = false,
                shippingAddress = "Av. Providencia 1234, Santiago",
                date = "2025-05-10",
                status = RequestStatus.CONFIRMED
            )
        )
    )
    val orders: StateFlow<List<RequestOrder>> = _orders.asStateFlow()

    // QR Scan Records
    private val _qrRecords = MutableStateFlow<List<QrRecord>>(
        listOf(
            QrRecord("qr1", "MUSEO-M1", "Museo del Prado Virtual", "Museo", "m1", "2025-05-12 10:30"),
            QrRecord("qr2", "PUB-P2", "Anuario de Museos de Iberoamérica", "Publicación", "p2", "2025-05-12 11:15")
        )
    )
    val qrRecords: StateFlow<List<QrRecord>> = _qrRecords.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem("n1", "Nuevo Museo Agregado", "Se integró la visita virtual del Museo Botero.", "Hace 2 horas", "map"),
            NotificationItem("n2", "Inscripción Abierta", "El taller de Mediación Cultural ya tiene cupos disponibles.", "Ayer", "catalog"),
            NotificationItem("n3", "Estado de Pedido Actualizado", "Tu pedido ORD-1001 ha sido Confirmado.", "Hace 3 días", "history")
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Autenticación: Inicio de sesión validando correo y contraseña
    fun login(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) return false
        val foundUser = _usersList.value.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password.trim()
        }
        return if (foundUser != null) {
            _currentUser.value = foundUser
            true
        } else {
            false
        }
    }

    // Registro de Usuario: Asignación AUTOMÁTICA y OBLIGATORIA de rol Cliente (UserRole.CLIENT)
    fun registerUser(name: String, email: String, password: String, avatarUrl: String): Boolean {
        if (name.isBlank() || email.isBlank() || password.isBlank()) return false

        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            password = password,
            role = UserRole.CLIENT, // Asignación de rol Cliente estricta y automática
            avatarUrl = avatarUrl.ifBlank { "https://picsum.photos/203" }
        )

        _usersList.update { it + newUser }
        // Se guarda el usuario registrado en el repositorio sin iniciar sesión automáticamente
        return true
    }

    fun selectUserRole(role: UserRole) {
        val found = _usersList.value.find { it.role == role } ?: defaultUsers[0]
        _currentUser.value = found
    }

    fun logout() {
        _currentUser.value = defaultUsers[0]
    }

    fun updateUserProfile(name: String, email: String, avatarUrl: String) {
        _currentUser.update {
            it.copy(
                name = name.ifBlank { it.name },
                email = email.ifBlank { it.email },
                avatarUrl = avatarUrl.ifBlank { it.avatarUrl }
            )
        }
        _usersList.update { list ->
            list.map { if (it.id == _currentUser.value.id) _currentUser.value else it }
        }
    }

    // Favorites Management
    fun toggleFavorite(museumId: String) {
        val uid = _currentUser.value.id
        _userFavorites.update { map ->
            val currentFavs = map[uid] ?: emptySet()
            val updatedFavs = if (currentFavs.contains(museumId)) {
                currentFavs - museumId
            } else {
                currentFavs + museumId
            }
            map + (uid to updatedFavs)
        }
    }

    fun isFavorite(museumId: String): Boolean {
        val uid = _currentUser.value.id
        return _userFavorites.value[uid]?.contains(museumId) ?: false
    }

    fun getFavoriteMuseums(): List<Museum> {
        val uid = _currentUser.value.id
        val favIds = _userFavorites.value[uid] ?: emptySet()
        return _museums.value.filter { favIds.contains(it.id) }
    }

    // Cart Management
    fun addToCart(product: ProductCourse, quantity: Int = 1) {
        _cart.update { currentList ->
            val existingIndex = currentList.indexOfFirst { it.productCourse.id == product.id }
            if (existingIndex >= 0) {
                currentList.mapIndexed { idx, item ->
                    if (idx == existingIndex) item.copy(quantity = item.quantity + quantity) else item
                }
            } else {
                currentList + CartItem(product, quantity)
            }
        }
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        _cart.update { list ->
            if (quantity <= 0) {
                list.filter { it.productCourse.id != productId }
            } else {
                list.map {
                    if (it.productCourse.id == productId) it.copy(quantity = quantity) else it
                }
            }
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun calculateShipping(isInternational: Boolean, itemsCount: Int): Double {
        if (itemsCount <= 0) return 0.0
        return if (isInternational) {
            25000.0 + (itemsCount * 3000.0)
        } else {
            5000.0 + (itemsCount * 1000.0)
        }
    }

    fun submitOrder(address: String, isInternational: Boolean): RequestOrder? {
        val currentItems = _cart.value
        if (currentItems.isEmpty() || address.isBlank()) return null

        val subtotal = currentItems.sumOf { it.productCourse.price * it.quantity }
        val itemsCount = currentItems.sumOf { it.quantity }
        val shipping = calculateShipping(isInternational, itemsCount)
        val total = subtotal + shipping

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val newOrder = RequestOrder(
            id = "ORD-${System.currentTimeMillis() % 10000}",
            userId = _currentUser.value.id,
            items = currentItems,
            totalAmount = total,
            shippingCost = shipping,
            isInternational = isInternational,
            shippingAddress = address,
            date = dateFormat.format(Date()),
            status = RequestStatus.PENDING
        )

        _orders.update { listOf(newOrder) + it }
        clearCart()
        
        // Add Notification
        addNotification(
            title = "Solicitud Enviada",
            message = "Tu orden ${newOrder.id} ha sido registrada exitosamente.",
            targetRoute = "history"
        )

        return newOrder
    }

    fun updateOrderStatus(orderId: String, newStatus: RequestStatus) {
        _orders.update { list ->
            list.map {
                if (it.id == orderId) it.copy(status = newStatus) else it
            }
        }
        addNotification(
            title = "Actualización de Orden",
            message = "La orden $orderId cambió a estado ${newStatus.label}.",
            targetRoute = "history"
        )
    }

    // QR Scan Handling
    fun processQrCode(rawCode: String): QrRecord {
        val codeUpper = rawCode.uppercase().trim()
        val record = when {
            codeUpper.contains("MUSEO") || codeUpper.contains("M1") -> {
                QrRecord(UUID.randomUUID().toString(), codeUpper, "Museo del Prado Virtual", "Museo", "m1", getCurrentTimestamp())
            }
            codeUpper.contains("PUB") || codeUpper.contains("P2") -> {
                QrRecord(UUID.randomUUID().toString(), codeUpper, "Anuario de Museos de Iberoamérica", "Publicación", "p2", getCurrentTimestamp())
            }
            else -> {
                QrRecord(UUID.randomUUID().toString(), codeUpper, "Recurso Cultural Generico", "Producto", "p1", getCurrentTimestamp())
            }
        }

        _qrRecords.update { current ->
            if (current.none { it.code == record.code }) {
                listOf(record) + current
            } else current
        }
        return record
    }

    fun addNotification(title: String, message: String, targetRoute: String? = null) {
        val newNotif = NotificationItem(
            id = UUID.randomUUID().toString(),
            title = title,
            message = message,
            timestamp = "Ahora",
            targetScreenRoute = targetRoute
        )
        _notifications.update { listOf(newNotif) + it }
    }

    fun markNotificationRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun addMuseum(museum: Museum) {
        _museums.update { it + museum }
    }

    fun addProductCourse(item: ProductCourse) {
        _catalog.update { it + item }
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
