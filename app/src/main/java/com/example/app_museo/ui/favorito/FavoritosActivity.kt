package com.example.app_museo.ui.favorito

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_museo.R
import com.example.app_museo.ui.detalle.DetalleMuseoActivity
import com.example.app_museo.ui.theme.App_museoTheme

data class MuseoFavorito(
    val id: Int,
    val nombre: String,
    val pais: String,
    val tipo: String,
    val imagenResId: Int
)

class FavoritosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.favoritos)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarFavoritos)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Mis Museos y Favoritos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewFavoritos).setContent {
            App_museoTheme {
                FavoritosScreen()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen() {
    val context = LocalContext.current
    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    val listaFavoritos = remember {
        mutableStateListOf(
            MuseoFavorito(1, "Museo Nacional de Bellas Artes", "Chile", "Museo", R.drawable.cat1),
            MuseoFavorito(2, "Guía de Arte Iberoamericano", "Chile", "Recurso", R.drawable.cat2),
            MuseoFavorito(3, "Museo Histórico Nacional", "Chile", "Museo", R.drawable.cat3)
        )
    }

    val elementosFiltrados = listaFavoritos.filter { item ->
        when (filtroSeleccionado) {
            "Museos" -> item.tipo.equals("Museo", ignoreCase = true)
            "Recursos" -> item.tipo.equals("Recurso", ignoreCase = true)
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Todos", "Museos", "Recursos").forEach { filtro ->
                FilterChip(
                    selected = filtroSeleccionado == filtro,
                    onClick = { filtroSeleccionado = filtro },
                    label = { Text(filtro) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (elementosFiltrados.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay elementos en esta categoría.", color = Color.Gray, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(elementosFiltrados, key = { it.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(context, DetalleMuseoActivity::class.java).apply {
                                    putExtra("EXTRA_NOMBRE", item.nombre)
                                }
                                context.startActivity(intent)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = item.imagenResId),
                                    contentDescription = item.nombre,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = item.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF1A202C)
                                )
                                Text(
                                    text = "${item.pais} • [${item.tipo}]",
                                    fontSize = 13.sp,
                                    color = Color(0xFF4A5568)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                TextButton(
                                    onClick = {
                                        Toast.makeText(context, "Iniciando visita virtual a ${item.nombre}...", Toast.LENGTH_SHORT).show()
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = "Visita 360°", color = Color(0xFFFF9800), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                TextButton(
                                    onClick = {
                                        listaFavoritos.remove(item)
                                        Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(text = "Quitar", color = Color(0xFFE53935), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
