package com.example.aplicacion_movil_museo.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.aplicacion_movil_museo.data.models.Lugar
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Locale

val PERMISOS_UBICACION = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

val FUENTE_MAPA_OSM = org.osmdroid.tileprovider.tilesource.XYTileSource(
    "OSM_FRANCE",
    0, 19, 256, ".png",
    arrayOf("https://a.tile.openstreetmap.fr/osmfr/", "https://b.tile.openstreetmap.fr/osmfr/", "https://c.tile.openstreetmap.fr/osmfr/")
)

fun Context.tienePermisoUbicacion(): Boolean {
    return PERMISOS_UBICACION.any {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Context.mostrarMensaje(mensaje: String) {
    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
}

@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    return remember {
        val config = Configuration.getInstance()
        config.userAgentValue = "AplicacionMuseoOrg/1.0"
        config.load(context, context.getSharedPreferences("osmdroid_pref_v3", Context.MODE_PRIVATE))
        MapView(context).apply {
            setTileSource(FUENTE_MAPA_OSM)
            setMultiTouchControls(true)
            zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(-33.4358, -70.6436))
        }
    }
}

@Composable
fun VistaMapa(mapView: MapView, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}

fun MapView.crearCapaMiUbicacion(): MyLocationNewOverlay {
    val overlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
    overlays.add(overlay)
    return overlay
}

fun MapView.crearMarcador(): Marker {
    val marker = Marker(this)
    overlays.add(marker)
    return marker
}

fun MapView.mostrarMarcador(marker: Marker, lugar: Lugar) {
    marker.position = lugar.punto
    marker.title = lugar.nombre
    marker.snippet = lugar.direccion
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    invalidate()
}

fun MapView.quitarMarcador(marker: Marker) {
    marker.position = null
    invalidate()
}

fun MapView.irA(punto: GeoPoint) {
    controller.animateTo(punto)
}

fun MapView.alMantenerPresionado(onLongPress: (GeoPoint) -> Unit) {
    val overlay = MapEventsOverlay(object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean = false
        override fun longPressHelper(p: GeoPoint?): Boolean {
            p?.let { onLongPress(it) }
            return true
        }
    })
    overlays.add(0, overlay)
}

fun Context.buscarDireccion(punto: GeoPoint): String? {
    return try {
        val geocoder = Geocoder(this, Locale.getDefault())
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocation(punto.latitude, punto.longitude, 1)
        addresses?.firstOrNull()?.getAddressLine(0)
    } catch (e: Exception) {
        null
    }
}

fun Context.buscarLugares(query: String): List<Lugar> {
    return try {
        val geocoder = Geocoder(this, Locale.getDefault())
        @Suppress("DEPRECATION")
        val addresses = geocoder.getFromLocationName(query, 5)
        addresses?.map {
            Lugar(
                nombre = it.featureName ?: query,
                direccion = it.getAddressLine(0) ?: "",
                punto = GeoPoint(it.latitude, it.longitude)
            )
        } ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraBusqueda(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    alBuscar: () -> Unit,
    buscando: Boolean,
    resultados: List<Lugar>,
    alElegir: (Lugar) -> Unit,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = texto,
            onValueChange = alCambiarTexto,
            placeholder = { Text("Buscar museo o dirección...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (texto.isNotEmpty()) {
                    IconButton(onClick = alLimpiar) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (resultados.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(resultados) { lugar ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { alElegir(lugar) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(lugar.nombre, fontWeight = FontWeight.Bold)
                                Text(lugar.direccion, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BotonesMapa(
    alAcercar: () -> Unit,
    alAlejar: () -> Unit,
    alIrAMiUbicacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = alIrAMiUbicacion,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Mi Ubicación", tint = MaterialTheme.colorScheme.primary)
        }

        FloatingActionButton(
            onClick = alAcercar,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = MaterialTheme.colorScheme.primary)
        }

        FloatingActionButton(
            onClick = alAlejar,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun TarjetaLugar(
    lugar: Lugar,
    color: Color,
    alCerrar: () -> Unit,
    alVerDetalle: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = color)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(lugar.nombre, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                IconButton(onClick = alCerrar) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            Text(lugar.direccion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            if (alVerDetalle != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = alVerDetalle,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Ficha Detallada del Museo")
                }
            }
        }
    }
}
