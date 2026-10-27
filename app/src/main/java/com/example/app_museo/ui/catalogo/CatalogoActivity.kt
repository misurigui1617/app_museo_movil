package com.example.app_museo.ui.catalogo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_museo.R
import com.example.app_museo.ui.theme.App_museoTheme

data class ProductoCurso(
    val id: Int,
    val nombre: String,
    val precio: String,
    val categoria: String,
    val disponibilidad: String,
    val imagenResId: Int
)

class CatalogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.catalogo)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarCatalogo)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Catálogo de Productos y Cursos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewCatalogo).setContent {
            App_museoTheme {
                CatalogoScreen(alVolver = { finish() })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_inicio -> {
                Toast.makeText(this, "Yendo a Inicio...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_mapa -> {
                Toast.makeText(this, "Abriendo Mapa...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_catalogo -> {
                Toast.makeText(this, "Ya te encuentras en el Catálogo", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_favoritos -> {
                val intent = Intent(this, com.example.app_museo.ui.favorito.FavoritosActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_notificaciones -> {
                Toast.makeText(this, "Abriendo Notificaciones...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_perfil -> {
                Toast.makeText(this, "Abriendo Perfil...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_qr_scanner -> {
                Toast.makeText(this, "Abriendo Escáner QR...", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(alVolver: () -> Unit) {
    val context = LocalContext.current
    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val listaCatalogo = listOf(
        ProductoCurso(1, "Curso de Curaduría Virtual", "$45.000 CLP", "Cursos", "En stock", R.drawable.cat1),
        ProductoCurso(2, "Historia del Arte Iberoamericano", "$32.000 CLP", "Libros", "En stock", R.drawable.cat2),
        ProductoCurso(3, "Kit de Conservación de Documentos", "$98.000 CLP", "Materiales", "Stock limitado", R.drawable.cat3),
        ProductoCurso(4, "Curso Museos y Comunidad", "$35.000 CLP", "Cursos", "En stock", R.drawable.cat4)
    )

    val categorias = listOf("Todos", "Cursos", "Libros", "Materiales")

    val elementosFiltrados = listaCatalogo.filter { item ->
        val coincideCat = categoriaSeleccionada == "Todos" || item.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        val coincideBusq = item.nombre.contains(textoBusqueda, ignoreCase = true)
        coincideCat && coincideBusq
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            placeholder = { Text("Buscar productos, cursos...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.9f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categorias.forEach { cat ->
                FilterChip(
                    selected = categoriaSeleccionada == cat,
                    onClick = { categoriaSeleccionada = cat },
                    label = { Text(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(elementosFiltrados) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    modifier = Modifier.clickable {

                        val intent = Intent(context, com.example.app_museo.ui.catalogo.CatalogoDetalleActivity::class.java).apply {
                            putExtra("EXTRA_NOMBRE", item.nombre)
                            putExtra("EXTRA_PRECIO", item.precio)
                            putExtra("EXTRA_IMAGEN", item.imagenResId)
                            putExtra("EXTRA_STOCK", item.disponibilidad)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = painterResource(id = item.imagenResId),
                                contentDescription = item.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            text = item.nombre,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.precio,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )

                            TextButton(
                                onClick = {
                                    Toast.makeText(context, "¡${item.nombre} agregado al carrito!", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Comprar",
                                    color = Color(0xFFFF9800),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
