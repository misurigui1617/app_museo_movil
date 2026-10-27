package com.example.app_museo.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.app_museo.Lugar
import com.example.app_museo.R
import com.example.app_museo.util.PUNTO_INICIAL
import com.example.app_museo.util.ZOOM_INICIAL
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

val OSM_FRANCE = object : OnlineTileSourceBase(
    "OpenStreetMapFrance",
    0,
    19,
    256,
    ".png",
    arrayOf(
        "https://a.tile.openstreetmap.fr/osmfr/",
        "https://b.tile.openstreetmap.fr/osmfr/",
        "https://c.tile.openstreetmap.fr/osmfr/"
    )
) {
    override fun getTileURLString(pMapTileIndex: Long): String {
        return baseUrl + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + mImageFilenameEnding
    }
}

@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            setTileSource(OSM_FRANCE)
            setMultiTouchControls(true)
            isTilesScaledToDpi = true

            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            minZoomLevel = 4.0
            maxZoomLevel = 19.0

            controller.setZoom(ZOOM_INICIAL)
            controller.setCenter(PUNTO_INICIAL)
        }
    }

    val cicloDeVida = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(cicloDeVida) {
        val observador = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        cicloDeVida.addObserver(observador)

        onDispose {
            cicloDeVida.removeObserver(observador)
            mapView.onDetach()
        }
    }

    return mapView
}

@Composable
fun VistaMapa(
    mapView: MapView,
    museos: List<Lugar> = emptyList(),
    alSeleccionarMuseo: (Lugar) -> Unit = {},
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { mapView },
        update = { map ->
            map.overlays.removeAll { it is Marker }

            val iconDrawable = ResourcesCompat.getDrawable(
                map.resources,
                R.drawable.ic_marcador,
                null
            )

            museos.forEach { museo ->
                val marker = Marker(map).apply {
                    position = museo.punto
                    title = museo.nombre
                    snippet = museo.direccion
                    icon = iconDrawable
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    setOnMarkerClickListener { _, _ ->
                        alSeleccionarMuseo(museo)
                        true
                    }
                }
                map.overlays.add(marker)
            }

            map.invalidate()
        },
        modifier = modifier
    )
}
