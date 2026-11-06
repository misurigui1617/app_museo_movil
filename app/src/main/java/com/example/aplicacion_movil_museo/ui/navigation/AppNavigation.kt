package com.example.aplicacion_movil_museo.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository
import com.example.aplicacion_movil_museo.ui.screens.autenticacion.*
import com.example.aplicacion_movil_museo.ui.screens.catalogo.*
import com.example.aplicacion_movil_museo.ui.screens.compras.*
import com.example.aplicacion_movil_museo.ui.screens.museos.*
import com.example.aplicacion_movil_museo.ui.screens.principal.*
import com.example.aplicacion_movil_museo.ui.screens.qr.*

data class ElementoBarraNavegacion(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var menuAtajosDesplegado by remember { mutableStateOf(false) }

    val bottomNavItems = listOf(
        ElementoBarraNavegacion(Screen.Principal, Icons.Default.Home, "Inicio"),
        ElementoBarraNavegacion(Screen.Mapa, Icons.Default.Map, "Mapa"),
        ElementoBarraNavegacion(Screen.Catalogo, Icons.Default.Storefront, "Catálogo"),
        ElementoBarraNavegacion(Screen.Notificaciones, Icons.Default.Notifications, "Avisos"),
        ElementoBarraNavegacion(Screen.Perfil, Icons.Default.Person, "Perfil")
    )

    val showMainNavigation = currentRoute in listOf(
        Screen.Principal.route,
        Screen.Mapa.route,
        Screen.Catalogo.route,
        Screen.Notificaciones.route,
        Screen.Perfil.route,
        Screen.Favoritos.route,
        Screen.Carrito.route,
        Screen.HistorialSolicitudes.route,
        Screen.EscanerQr.route
    )

    Scaffold(
        topBar = {
            if (showMainNavigation) {
                TopAppBar(
                    title = { Text("Museo de los Museos", fontWeight = FontWeight.Bold) },
                    actions = {
                        // --- REQUERIMIENTO 1: MENÚ DESPLEGABLE CON ATAJOS RÁPIDOS ---
                        IconButton(onClick = { menuAtajosDesplegado = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menú de Atajos")
                        }

                        DropdownMenu(
                            expanded = menuAtajosDesplegado,
                            onDismissRequest = { menuAtajosDesplegado = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Inicio") },
                                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Principal.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Catálogo") },
                                leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Catalogo.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Favoritos") },
                                leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Favoritos.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Carrito") },
                                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Carrito.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Mapa de Museos") },
                                leadingIcon = { Icon(Icons.Default.Map, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Mapa.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Escáner QR") },
                                leadingIcon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.EscanerQr.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Historial de Solicitudes") },
                                leadingIcon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.HistorialSolicitudes.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Notificaciones") },
                                leadingIcon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Notificaciones.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Perfil") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                onClick = {
                                    menuAtajosDesplegado = false
                                    navController.navigate(Screen.Perfil.route)
                                }
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showMainNavigation) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.screen.route
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Bienvenida.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Pantalla de Bienvenida
            composable(Screen.Bienvenida.route) {
                PantallaBienvenida(
                    onComenzarClick = {
                        navController.navigate(Screen.InicioSesion.route)
                    }
                )
            }

            // 2. Inicio de Sesión
            composable(Screen.InicioSesion.route) {
                PantallaInicioSesion(
                    onInicioSesionExitoso = {
                        navController.navigate(Screen.Principal.route) {
                            popUpTo(Screen.Bienvenida.route) { inclusive = true }
                        }
                    },
                    onNavegarARegistro = {
                        navController.navigate(Screen.Registro.route)
                    }
                )
            }

            // 3. Registro
            composable(Screen.Registro.route) {
                val context = androidx.compose.ui.platform.LocalContext.current
                PantallaRegistro(
                    onRegistroExitoso = {
                        android.widget.Toast.makeText(
                            context,
                            "Cuenta creada con éxito. Por favor, ingresa con tus credenciales.",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(Screen.InicioSesion.route) {
                            popUpTo(Screen.Registro.route) { inclusive = true }
                        }
                    },
                    onNavegarAInicioSesion = {
                        navController.popBackStack()
                    }
                )
            }

            // 4. Principal
            composable(Screen.Principal.route) {
                PantallaPrincipal(
                    onNavegarADetalleMuseo = { museumId ->
                        navController.navigate(Screen.DetalleMuseo.crearRuta(museumId))
                    },
                    onNavegarAMapa = {
                        navController.navigate(Screen.Mapa.route)
                    },
                    onNavegarACatalogo = {
                        navController.navigate(Screen.Catalogo.route)
                    },
                    onNavegarAEscanerQr = {
                        navController.navigate(Screen.EscanerQr.route)
                    },
                    onNavegarACarrito = {
                        navController.navigate(Screen.Carrito.route)
                    },
                    onNavegarAHistorial = {
                        navController.navigate(Screen.HistorialSolicitudes.route)
                    },
                    onNavegarAFavoritos = {
                        navController.navigate(Screen.Favoritos.route)
                    }
                )
            }

            // 5. Catálogo
            composable(Screen.Catalogo.route) {
                PantallaCatalogo(
                    onNavegarADetalleProducto = { productId ->
                        navController.navigate(Screen.DetalleProducto.crearRuta(productId))
                    }
                )
            }

            // 6. Detalle Producto
            composable(
                route = Screen.DetalleProducto.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                PantallaDetalleProducto(
                    productId = productId,
                    onVolver = { navController.popBackStack() },
                    onNavegarACarrito = { navController.navigate(Screen.Carrito.route) }
                )
            }

            // 7. Detalle Museo
            composable(
                route = Screen.DetalleMuseo.route,
                arguments = listOf(navArgument("museumId") { type = NavType.StringType })
            ) { backStackEntry ->
                val museumId = backStackEntry.arguments?.getString("museumId") ?: ""
                PantallaDetalleMuseo(
                    museumId = museumId,
                    onVolver = { navController.popBackStack() }
                )
            }

            // 8. Favoritos
            composable(Screen.Favoritos.route) {
                PantallaFavoritos(
                    onNavegarADetalleMuseo = { museumId ->
                        navController.navigate(Screen.DetalleMuseo.crearRuta(museumId))
                    }
                )
            }

            // 9. Mapa
            composable(Screen.Mapa.route) {
                PantallaMapa(
                    onNavegarADetalle = { museumId ->
                        navController.navigate(Screen.DetalleMuseo.crearRuta(museumId))
                    }
                )
            }

            // 10. Carrito
            composable(Screen.Carrito.route) {
                PantallaCarrito(
                    onNavegarAHistorial = {
                        navController.navigate(Screen.HistorialSolicitudes.route)
                    },
                    onNavegarACatalogo = {
                        navController.navigate(Screen.Catalogo.route)
                    }
                )
            }

            // 11. Historial
            composable(Screen.HistorialSolicitudes.route) {
                PantallaHistorialSolicitudes()
            }

            // 12. Notificaciones
            composable(Screen.Notificaciones.route) {
                PantallaNotificaciones(
                    onNavegarARuta = { route ->
                        navController.navigate(route)
                    }
                )
            }

            // 13. Perfil
            composable(Screen.Perfil.route) {
                PantallaPerfil(
                    onCerrarSesionOCambiarRol = {
                        MuseoRepository.logout()
                        navController.navigate(Screen.InicioSesion.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 14. Escáner QR
            composable(Screen.EscanerQr.route) {
                PantallaEscanerQr(
                    onQrEscaneadoExito = { code ->
                        navController.navigate(Screen.ResultadoQr.crearRuta(code)) {
                            popUpTo(Screen.EscanerQr.route) { inclusive = true }
                        }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }

            // 15. Resultado QR
            composable(
                route = Screen.ResultadoQr.route,
                arguments = listOf(navArgument("code") { type = NavType.StringType })
            ) { backStackEntry ->
                val rawCode = backStackEntry.arguments?.getString("code") ?: ""
                val decodedCode = android.net.Uri.decode(rawCode)
                PantallaResultadoQr(
                    scannedCode = decodedCode,
                    onNavegarADetalleMuseo = { museumId ->
                        navController.navigate(Screen.DetalleMuseo.crearRuta(museumId))
                    },
                    onNavegarACatalogo = {
                        navController.navigate(Screen.Catalogo.route)
                    },
                    onReescanear = {
                        navController.navigate(Screen.EscanerQr.route) {
                            popUpTo(Screen.ResultadoQr.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
