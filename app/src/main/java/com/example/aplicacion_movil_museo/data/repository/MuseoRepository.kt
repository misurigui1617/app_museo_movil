package com.example.aplicacion_movil_museo.data.repository

import com.example.aplicacion_movil_museo.R
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

    // Museums Initial Data con asignación de imágenes locales de res/drawable/
    private val initialMuseums = listOf(
        Museum(
            id = "m_mmdh",
            name = "Museo de la Memoria y los Derechos Humanos",
            country = "Chile",
            city = "Santiago",
            description = "Espacio destinado a visibilizar las violaciones a los derechos humanos del Estado de Chile entre 1973 y 1990.",
            classification = "Derechos Humanos",
            virtualTourUrl = "https://mmdh.cl/museo",
            imageResId = R.drawable.m_mmdh,
            latitude = -33.4428,
            longitude = -70.6781,
            isFavorite = true
        ),
        Museum(
            id = "m3",
            name = "Museo Nacional de Bellas Artes",
            country = "Chile",
            city = "Santiago",
            description = "Principal centro de difusión de las artes visuales en Chile con exposiciones de pintura, escultura y patrimonio artístico.",
            classification = "Bellas Artes",
            virtualTourUrl = "https://www.mnba.gob.cl/virtual",
            imageResId = R.drawable.m3,
            latitude = -33.4358,
            longitude = -70.6436,
            isFavorite = true
        ),
        Museum(
            id = "m5",
            name = "Museo Histórico Nacional",
            country = "Chile",
            city = "Santiago",
            description = "Institución encargada de divulgar la historia de Chile desde la época prehispánica.",
            classification = "Historia",
            virtualTourUrl = "https://www.mhn.gob.cl/virtual",
            imageResId = R.drawable.m5,
            latitude = -33.4380,
            longitude = -70.6510,
            isFavorite = false
        ),
        Museum(
            id = "m6",
            name = "Museo de Arte Contemporáneo MAC",
            country = "Chile",
            city = "Santiago",
            description = "Espacio de investigación y difusión de artes visuales contemporáneas en Chile.",
            classification = "Arte Contemporáneo",
            virtualTourUrl = "https://www.mac.uchile.cl/virtual",
            imageResId = R.drawable.m6,
            latitude = -33.4352,
            longitude = -70.6440,
            isFavorite = true
        ),
        Museum(
            id = "m7",
            name = "Museo Nacional de Bellas Artes (MNBA)",
            country = "Argentina",
            city = "Buenos Aires",
            description = "Uno de los museos más importantes de América Latina con obras maestras universales.",
            classification = "Bellas Artes",
            virtualTourUrl = "https://www.bellasartes.gob.ar/virtual",
            imageResId = R.drawable.m7,
            latitude = -34.5839,
            longitude = -58.3931,
            isFavorite = false
        ),
        Museum(
            id = "m2",
            name = "Museo Tamayo de Arte Contemporáneo",
            country = "México",
            city = "Ciudad de México",
            description = "Espacio dedicado a las expresiones artísticas contemporáneas y vanguardias internacionales.",
            classification = "Arte Contemporáneo",
            virtualTourUrl = "https://museotamayo.org/virtual",
            imageResId = R.drawable.m2,
            latitude = 19.4260,
            longitude = -99.1812,
            isFavorite = false
        ),
        Museum(
            id = "m8",
            name = "Museo Frida Kahlo (La Casa Azul)",
            country = "México",
            city = "Ciudad de México",
            description = "Universo íntimo y legado artístico de Frida Kahlo y Diego Rivera.",
            classification = "Biográfico",
            virtualTourUrl = "https://www.museofridakahlo.org.mx/virtual",
            imageResId = R.drawable.m8,
            latitude = 19.3551,
            longitude = -99.1625,
            isFavorite = true
        )
    )

    private val _museums = MutableStateFlow<List<Museum>>(initialMuseums)
    val museums: StateFlow<List<Museum>> = _museums.asStateFlow()

    // Favorites per user ID
    private val _userFavorites = MutableStateFlow<Map<String, Set<String>>>(
        mapOf("1" to setOf("m_mmdh", "m3"))
    )

    private val _productFavorites = MutableStateFlow<Map<String, Set<String>>>(
        mapOf("1" to setOf("p1", "p3"))
    )

    // Catalog Initial Data con asignación de imágenes locales cat1.jpg a cat4.jpg y stock
    private val initialCatalog = listOf(
        ProductCourse(
            id = "p1",
            title = "Curso: Curaduría y Exposición Museográfica",
            category = "Cursos",
            price = 45000.0,
            description = "Capacitación profesional sobre diseño de exhibiciones, narrativa visual y conservación.",
            type = ItemType.COURSE,
            stock = 12,
            imageResId = R.drawable.cat1
        ),
        ProductCourse(
            id = "p2",
            title = "Publicación: Anuario de Museos de Iberoamérica",
            category = "Publicaciones",
            price = 18500.0,
            description = "Edición impresa con análisis de más de 120 galerías y centros culturales regionales.",
            type = ItemType.PRODUCT,
            stock = 25,
            imageResId = R.drawable.cat2
        ),
        ProductCourse(
            id = "p3",
            title = "Kit de Conservación Preventiva de Papel",
            category = "Materiales",
            price = 32000.0,
            description = "Insumos especializados libre de ácido para la preservación de manuscritos e impresos.",
            type = ItemType.PRODUCT,
            stock = 8,
            imageResId = R.drawable.cat3
        ),
        ProductCourse(
            id = "p4",
            title = "Taller Virtual: Mediación Cultural y Audiencias",
            category = "Cursos",
            price = 28000.0,
            description = "Estrategias participativas para la fidelización de visitantes e integración escolar.",
            type = ItemType.COURSE,
            stock = 15,
            imageResId = R.drawable.cat4
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
            QrRecord("qr1", "MMDH-CHILE", "Museo de la Memoria y los Derechos Humanos", "Museo", "m_mmdh", "2025-05-12 10:30"),
            QrRecord("qr2", "PUB-P2", "Anuario de Museos de Iberoamérica", "Publicación", "p2", "2025-05-12 11:15")
        )
    )
    val qrRecords: StateFlow<List<QrRecord>> = _qrRecords.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem("n1", "Nuevo Museo Agregado", "Se integró el Museo de la Memoria y los Derechos Humanos.", "Hace 2 horas", "map"),
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

    // Registro de Usuario
    fun registerUser(name: String, email: String, password: String, avatarUrl: String): Boolean {
        if (name.isBlank() || email.isBlank() || password.isBlank()) return false

        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            password = password,
            role = UserRole.CLIENT,
            avatarUrl = avatarUrl.ifBlank { "https://picsum.photos/203" }
        )

        _usersList.update { it + newUser }
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

    fun obtenerMuseos(): List<Museum> = _museums.value

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

    // Product Favorites Management
    fun toggleProductFavorite(productId: String) {
        val uid = _currentUser.value.id
        _productFavorites.update { map ->
            val currentFavs = map[uid] ?: emptySet()
            val updatedFavs = if (currentFavs.contains(productId)) {
                currentFavs - productId
            } else {
                currentFavs + productId
            }
            map + (uid to updatedFavs)
        }
    }

    fun isProductFavorite(productId: String): Boolean {
        val uid = _currentUser.value.id
        return _productFavorites.value[uid]?.contains(productId) ?: false
    }

    fun getFavoriteProducts(): List<ProductCourse> {
        val uid = _currentUser.value.id
        val favIds = _productFavorites.value[uid] ?: emptySet()
        return _catalog.value.filter { favIds.contains(it.id) }
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

    // --- LÓGICA DE PROCESAMIENTO Y VALIDACIÓN DE CÓDIGOS QR ---
    fun processQrCode(rawCode: String): QrRecord? {
        if (rawCode.isBlank()) return null
        val codeUpper = rawCode.uppercase().trim()

        val record = when {
            codeUpper.contains("MMDH") || codeUpper.contains("DERECHOS") || codeUpper.contains("MMDH.CL") -> {
                QrRecord(
                    id = UUID.randomUUID().toString(),
                    code = "MMDH-CHILE",
                    title = "Museo de la Memoria y los Derechos Humanos",
                    type = "Museo",
                    targetId = "m_mmdh",
                    timestamp = getCurrentTimestamp()
                )
            }
            codeUpper.contains("MUSEO") || codeUpper.contains("M3") || codeUpper.contains("BELLAS ARTES") -> {
                QrRecord(
                    id = UUID.randomUUID().toString(),
                    code = "MUSEO-M3",
                    title = "Museo Nacional de Bellas Artes",
                    type = "Museo",
                    targetId = "m3",
                    timestamp = getCurrentTimestamp()
                )
            }
            codeUpper.contains("PUB") || codeUpper.contains("P2") || codeUpper.contains("ANUARIO") -> {
                QrRecord(
                    id = UUID.randomUUID().toString(),
                    code = "PUB-P2",
                    title = "Anuario de Museos de Iberoamérica",
                    type = "Publicación",
                    targetId = "p2",
                    timestamp = getCurrentTimestamp()
                )
            }
            else -> {
                null
            }
        }

        if (record != null) {
            _qrRecords.update { current ->
                if (current.none { it.code == record.code }) {
                    listOf(record) + current
                } else current
            }
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
