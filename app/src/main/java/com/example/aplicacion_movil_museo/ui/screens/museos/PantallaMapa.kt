package com.example.aplicacion_movil_museo.ui.screens.museos

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.models.Lugar
import com.example.aplicacion_movil_museo.data.models.Museum
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository
import com.example.aplicacion_movil_museo.ui.components.*
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

// =====================================================================
// PantallaMapa.kt - "Museo de los Museos"
// ---------------------------------------------------------------------
// Pantalla principal interactiva con OSMDroid (OpenStreetMap). Muestra los
// museos destacados (Chile, Argentina, México) con filtro por país,
// marcadores de ubicación, menú desplegable y tarjeta flotante interactiva.
// =====================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(
    onNavegarADetalle: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val corrutinas = rememberCoroutineScope()

    // 1. EL MAPA Y SUS CAPAS
    val mapa = rememberMapView()
    val capaMiUbicacion = remember { mapa.crearCapaMiUbicacion() }
    val marcador = remember { mapa.crearMarcador() }

    // Lista de museos desde MuseoRepository (Chile, Argentina, México)
    val museosOriginales by MuseoRepository.museums.collectAsState()

    // 2. ESTADO DE FILTROS Y MENÚ
    var filtroPais by remember { mutableStateOf("Todos") }
    var menuMuseosDesplegado by remember { mutableStateOf(false) }

    val paises = remember(museosOriginales) {
        listOf("Todos", "Chile", "Argentina", "México")
    }

    val museosFiltrados = remember(filtroPais, museosOriginales) {
        if (filtroPais == "Todos") museosOriginales
        else museosOriginales.filter { it.country.equals(filtroPais, ignoreCase = true) }
    }

    var textoBusqueda by remember { mutableStateOf("") }
    var buscando by remember { mutableStateOf(false) }
    var resultados by remember { mutableStateOf(listOf<Lugar>()) }

    var lugarEnTarjeta by remember { mutableStateOf<Lugar?>(null) }
    var museoSeleccionado by remember { mutableStateOf<Museum?>(null) }
    var tarjetaEsMiUbicacion by remember { mutableStateOf(false) }

    // 3. ACCIONES DE SELECCIÓN Y UBICACIÓN
    fun seleccionarLugar(lugar: Lugar, museo: Museum? = null) {
        mapa.mostrarMarcador(marcador, lugar)
        mapa.irA(lugar.punto)
        lugarEnTarjeta = lugar
        museoSeleccionado = museo
        tarjetaEsMiUbicacion = false
        resultados = emptyList()
    }

    fun seleccionarMuseo(museo: Museum) {
        val lugar = Lugar(
            nombre = museo.name,
            direccion = "${museo.city}, ${museo.country} - ${museo.classification}",
            punto = GeoPoint(museo.latitude, museo.longitude)
        )
        seleccionarLugar(lugar, museo)
    }

    fun mostrarMiUbicacion(punto: GeoPoint) {
        mapa.irA(punto)
        lugarEnTarjeta = Lugar("Tú estás aquí", "Tu posición según el GPS", punto)
        museoSeleccionado = null
        tarjetaEsMiUbicacion = true
    }

    fun buscarMiUbicacion() {
        capaMiUbicacion.enableMyLocation()
        val puntoConocido = capaMiUbicacion.myLocation
        if (puntoConocido != null) {
            mostrarMiUbicacion(puntoConocido)
        } else {
            context.mostrarMensaje("Buscando tu ubicación…")
            capaMiUbicacion.runOnFirstFix {
                mapa.post { capaMiUbicacion.myLocation?.let { mostrarMiUbicacion(it) } }
            }
        }
    }

    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { respuestas ->
        if (respuestas.values.any { it }) {
            buscarMiUbicacion()
        } else {
            context.mostrarMensaje("Sin permiso de ubicación no puedo mostrar dónde estás")
        }
    }

    fun alTocarMiUbicacion() {
        if (context.tienePermisoUbicacion()) {
            buscarMiUbicacion()
        } else {
            pedirPermiso.launch(PERMISOS_UBICACION)
        }
    }

    LaunchedEffect(Unit) {
        if (context.tienePermisoUbicacion()) {
            buscarMiUbicacion()
        }

        mapa.alMantenerPresionado { punto ->
            seleccionarLugar(Lugar("Punto elegido", "Buscando dirección…", punto), null)
            corrutinas.launch {
                val direccion = context.buscarDireccion(punto) ?: "Dirección desconocida"
                seleccionarLugar(Lugar("Punto elegido", direccion, punto), null)
            }
        }
    }

    fun buscar() {
        if (textoBusqueda.isBlank()) return

        corrutinas.launch {
            buscando = true
            val coincidenciasMuseos = museosFiltrados.filter {
                it.name.contains(textoBusqueda, ignoreCase = true) ||
                        it.city.contains(textoBusqueda, ignoreCase = true) ||
                        it.country.contains(textoBusqueda, ignoreCase = true)
            }.map { Lugar(it.name, "${it.city}, ${it.country}", GeoPoint(it.latitude, it.longitude)) }

            val resultadosGeocoder = context.buscarLugares(textoBusqueda)

            resultados = (coincidenciasMuseos + resultadosGeocoder).distinctBy { it.nombre }
            buscando = false

            if (resultados.isEmpty()) {
                context.mostrarMensaje("No se encontraron resultados para \"$textoBusqueda\"")
            }
        }
    }

    // 4. INTERFAZ DE USUARIO
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Mapa interactivo de fondo
        VistaMapa(mapView = mapa, modifier = Modifier.fillMaxSize())

        // Panel Superior: Barra de Búsqueda y Filtro de Países
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            BarraBusqueda(
                texto = textoBusqueda,
                alCambiarTexto = { textoBusqueda = it },
                alBuscar = { buscar() },
                buscando = buscando,
                resultados = resultados,
                alElegir = { lugarElegido ->
                    val museoCoincidente = museosFiltrados.find { it.name == lugarElegido.nombre }
                    seleccionarLugar(lugarElegido, museoCoincidente)
                },
                alLimpiar = {
                    textoBusqueda = ""
                    resultados = emptyList()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selector / Pestañas de Filtro por Países con Botón de Menú Desplegable
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "País:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(paises) { pais ->
                            FilterChip(
                                selected = filtroPais == pais,
                                onClick = {
                                    filtroPais = pais
                                    lugarEnTarjeta = null
                                    museoSeleccionado = null
                                },
                                label = { Text(pais, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }

                    // Botón Flotante para Menú Desplegable de Museos
                    Box {
                        IconButton(onClick = { menuMuseosDesplegado = true }) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Lista de Museos",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = menuMuseosDesplegado,
                            onDismissRequest = { menuMuseosDesplegado = false }
                        ) {
                            Text(
                                text = "Museos Destacados (${museosFiltrados.size})",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider()

                            museosFiltrados.forEach { museo ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(museo.name, fontWeight = FontWeight.Bold)
                                            Text("${museo.city}, ${museo.country}", style = MaterialTheme.typography.bodySmall)
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Museum, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    },
                                    onClick = {
                                        menuMuseosDesplegado = false
                                        seleccionarMuseo(museo)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Panel Inferior: Botones de Zoom/GPS y Tarjeta Flotante
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "© OpenStreetMap",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                BotonesMapa(
                    alAcercar = { mapa.controller.zoomIn() },
                    alAlejar = { mapa.controller.zoomOut() },
                    alIrAMiUbicacion = { alTocarMiUbicacion() }
                )
            }

            // Tarjeta Flotante Informativa del Museo Seleccionado
            lugarEnTarjeta?.let { lugar ->
                TarjetaLugar(
                    lugar = lugar,
                    color = MaterialTheme.colorScheme.primary,
                    alCerrar = {
                        lugarEnTarjeta = null
                        museoSeleccionado = null
                        if (!tarjetaEsMiUbicacion) mapa.quitarMarcador(marcador)
                    },
                    alVerDetalle = museoSeleccionado?.let { museo ->
                        { onNavegarADetalle(museo.id) }
                    }
                )
            }
        }
    }
}
