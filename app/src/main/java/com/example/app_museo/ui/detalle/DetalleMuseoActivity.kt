package com.example.app_museo.ui.detalle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_museo.ui.theme.App_museoTheme
import com.example.app_museo.ui.theme.RojoMarcador

class DetalleMuseoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val nombre = intent.getStringExtra("MUSEO_NOMBRE") ?: "Museo Virtual"
        val direccion = intent.getStringExtra("MUSEO_DIRECCION") ?: "Dirección no disponible"
        val pais = intent.getStringExtra("MUSEO_PAIS") ?: "Chile"
        val categoria = intent.getStringExtra("MUSEO_CATEGORIA") ?: "Patrimonio"
        val valoracion = intent.getDoubleExtra("MUSEO_VALORACION", 4.8)
        val descripcion = intent.getStringExtra("MUSEO_DESCRIPCION") ?: "Patrimonio histórico y cultural destacado de Iberoamérica."
        val urlRecorrido = intent.getStringExtra("MUSEO_URL") ?: "https://artsandculture.google.com/"

        setContent {
            App_museoTheme {
                DetalleMuseoScreen(
                    nombre = nombre,
                    direccion = direccion,
                    pais = pais,
                    categoria = categoria,
                    valoracion = valoracion,
                    descripcion = descripcion,
                    urlRecorrido = urlRecorrido,
                    alVolver = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMuseoScreen(
    nombre: String,
    direccion: String,
    pais: String,
    categoria: String,
    valoracion: Double,
    descripcion: String,
    urlRecorrido: String,
    alVolver: () -> Unit
) {
    val context = LocalContext.current
    var esFavorito by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Museo") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        esFavorito = !esFavorito
                        val msg = if (esFavorito) "Añadido a favoritos" else "Quitado de favoritos"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = if (esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (esFavorito) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SuggestionChip(onClick = {}, label = { Text(categoria) })
                Text(
                    text = pais,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = nombre,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = RojoMarcador,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = direccion, style = MaterialTheme.typography.bodyMedium)
            }

            Text(
                text = "⭐ Valoración: $valoracion / 5.0",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Divider()

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlRecorrido))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Ver proyecto / Recorrido virtual 🌐", fontWeight = FontWeight.Bold)
            }
        }
    }
}
