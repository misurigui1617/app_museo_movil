package com.example.app_museo.ui.mapa

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.example.app_museo.Lugar
import com.example.app_museo.data.DatosPruebaMuseos
import com.example.app_museo.ui.components.BarraBusqueda
import com.example.app_museo.ui.components.BotonesMapa
import com.example.app_museo.ui.components.FiltrosMapa
import com.example.app_museo.ui.components.TarjetaVistaPreviaMuseo
import com.example.app_museo.ui.components.VistaMapa
import com.example.app_museo.ui.components.mostrarMensaje
import com.example.app_museo.ui.components.rememberMapView
import com.example.app_museo.ui.theme.App_museoTheme
import org.osmdroid.config.Configuration

class MapaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        Configuration.getInstance().load(applicationContext, prefs)
        Configuration.getInstance().userAgentValue = "AppMuseoChile/1.0"

        cacheDir.resolve("osmdroid").deleteRecursively()

        enableEdgeToEdge()
        setContent {
            App_museoTheme {
                val mapView = rememberMapView()

                var textoBusqueda by remember { mutableStateOf("") }
                var filtroSeleccionado by remember { mutableStateOf("Todos") }
                var museoSeleccionado by remember { mutableStateOf<Lugar?>(null) }

                val museosFiltrados = remember(textoBusqueda, filtroSeleccionado) {
                    DatosPruebaMuseos.listaMuseos.filter { museo ->
                        val coincideFiltro = when (filtroSeleccionado) {
                            "Todos" -> true
                            "País" -> museo.categoria == "País"
                            "Proyecto" -> museo.categoria == "Proyecto"
                            else -> museo.pais.equals(filtroSeleccionado, ignoreCase = true)
                        }
                        val coincideBusqueda = textoBusqueda.isBlank() ||
                                museo.nombre.contains(textoBusqueda, ignoreCase = true) ||
                                museo.direccion.contains(textoBusqueda, ignoreCase = true) ||
                                museo.pais.contains(textoBusqueda, ignoreCase = true)

                        coincideFiltro && coincideBusqueda
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    VistaMapa(
                        mapView = mapView,
                        museos = museosFiltrados,
                        alSeleccionarMuseo = { museo ->
                            museoSeleccionado = museo
                            mapView.controller.animateTo(museo.punto)
                            mapView.controller.setZoom(16.0)
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .statusBarsPadding()
                            .padding(top = 8.dp)
                    ) {
                        BarraBusqueda(
                            texto = textoBusqueda,
                            alCambiarTexto = { nuevoTexto ->
                                textoBusqueda = nuevoTexto
                            },
                            alBuscar = {
                                if (museosFiltrados.isNotEmpty()) {
                                    val primerResultado = museosFiltrados.first()
                                    museoSeleccionado = primerResultado
                                    mapView.controller.animateTo(primerResultado.punto)
                                    mapView.controller.setZoom(16.0)
                                }
                            },
                            buscando = false,
                            resultados = if (textoBusqueda.isNotBlank()) museosFiltrados else emptyList(),
                            alElegir = { lugarElegido ->
                                textoBusqueda = ""
                                museoSeleccionado = lugarElegido
                                mapView.controller.animateTo(lugarElegido.punto)
                                mapView.controller.setZoom(16.0)
                            },
                            alLimpiar = {
                                textoBusqueda = ""
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FiltrosMapa(
                            opciones = DatosPruebaMuseos.categoriasFiltro,
                            opcionSeleccionada = filtroSeleccionado,
                            alSeleccionarOpcion = { nuevaOpcion ->
                                filtroSeleccionado = nuevaOpcion
                                museoSeleccionado = null
                            }
                        )
                    }

                    BotonesMapa(
                        alAcercar = {
                            mapView.controller.zoomIn()
                        },
                        alAlejar = {
                            mapView.controller.zoomOut()
                        },
                        alIrAMiUbicacion = {
                            val puntoSantiago = DatosPruebaMuseos.listaMuseos.first().punto
                            mapView.controller.animateTo(puntoSantiago)
                            mapView.controller.setZoom(15.0)
                            mostrarMensaje("Centrando en Santiago, Chile")
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                bottom = if (museoSeleccionado != null) 210.dp else 24.dp,
                                end = 16.dp
                            )
                    )

                    museoSeleccionado?.let { museo ->
                        TarjetaVistaPreviaMuseo(
                            museo = museo,
                            alCerrar = {
                                museoSeleccionado = null
                            },
                            alVerDetalle = { lugar ->
                                val intent = Intent(this@MapaActivity, com.example.app_museo.ui.detalle.DetalleMuseoActivity::class.java).apply {
                                    putExtra("MUSEO_NOMBRE", lugar.nombre)
                                    putExtra("MUSEO_DIRECCION", lugar.direccion)
                                    putExtra("MUSEO_PAIS", lugar.pais)
                                    putExtra("MUSEO_CATEGORIA", lugar.categoria)
                                    putExtra("MUSEO_VALORACION", lugar.valoracion)
                                    putExtra("MUSEO_DESCRIPCION", lugar.descripcion)
                                    putExtra("MUSEO_URL", lugar.imagenUrl)
                                }
                                startActivity(intent)
                            },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
