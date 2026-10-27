package com.example.app_museo.ui.carrito

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_museo.R
import com.example.app_museo.ui.theme.App_museoTheme


data class ItemCarrito(
    val id: Int,
    val nombre: String,
    val precioUnitario: Int,
    var cantidad: Int
)

class CarritoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.carrito)

        val toolbar = findViewById<Toolbar>(R.id.toolbarCarrito)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Carrito y Cálculo de Envío"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewCarrito).setContent {
            App_museoTheme {
                CarritoScreen()
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
fun CarritoScreen() {
    val context = LocalContext.current


    val listaCarrito = remember {
        mutableStateListOf(
            ItemCarrito(1, "Curso de Curaduría Virtual", 45000, 1),
            ItemCarrito(2, "Historia del Arte Iberoamericano", 32000, 2)
        )
    }


    var destinoSeleccionado by remember { mutableStateOf("Nacional (Chile)") } // "Nacional (Chile)" o "Internacional"
    var modalidadEnvio by remember { mutableStateOf("Estándar") } // "Estándar" o "Expreso"


    val subtotal = listaCarrito.sumOf { it.precioUnitario * it.cantidad }


    val costoEnvio = when {
        listaCarrito.isEmpty() -> 0
        destinoSeleccionado == "Nacional (Chile)" -> if (modalidadEnvio == "Estándar") 4500 else 8000
        else -> if (modalidadEnvio == "Estándar") 18000 else 32000
    }

    val totalFinal = subtotal + costoEnvio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Resumen de Productos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A202C)
        )


        if (listaCarrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Tu carrito está vacío", color = Color.Gray)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listaCarrito.forEach { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = item.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "$${item.precioUnitario} CLP c/u", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "Subtotal: $${item.precioUnitario * item.cantidad} CLP",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (item.cantidad > 1) {
                                            val index = listaCarrito.indexOf(item)
                                            listaCarrito[index] = item.copy(cantidad = item.cantidad - 1)
                                        } else {
                                            listaCarrito.remove(item)
                                        }
                                    },
                                    modifier = Modifier.size(32.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0))
                                ) {
                                    Text("-", color = Color.Black, fontWeight = FontWeight.Bold)
                                }

                                Text(
                                    text = "${item.cantidad}",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                Button(
                                    onClick = {
                                        val index = listaCarrito.indexOf(item)
                                        listaCarrito[index] = item.copy(cantidad = item.cantidad + 1)
                                    },
                                    modifier = Modifier.size(32.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A))
                                ) {
                                    Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = Color(0xFFE2E8F0))


        Text(
            text = "Opciones de Envío",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A202C)
        )


        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Destino:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = destinoSeleccionado == "Nacional (Chile)",
                    onClick = { destinoSeleccionado = "Nacional (Chile)" },
                    label = { Text("Nacional (Chile)") }
                )
                FilterChip(
                    selected = destinoSeleccionado == "Internacional",
                    onClick = { destinoSeleccionado = "Internacional" },
                    label = { Text("Internacional") }
                )
            }
        }


        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Modalidad:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = modalidadEnvio == "Estándar",
                    onClick = { modalidadEnvio = "Estándar" },
                    label = { Text("Estándar (3-5 días)") }
                )
                FilterChip(
                    selected = modalidadEnvio == "Expreso",
                    onClick = { modalidadEnvio = "Expreso (24-48 hrs)" },
                    label = { Text("Expreso") }
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal productos:", fontSize = 14.sp)
                    Text("$${subtotal} CLP", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Costo de envío estimado:", fontSize = 14.sp)
                    Text("$${costoEnvio} CLP", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFFF9800))
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Monto Total Final:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("$${totalFinal} CLP", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            }
        }

        Button(
            onClick = {
                if (listaCarrito.isEmpty()) {
                    Toast.makeText(context, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "¡Solicitud de pedido enviada! El backend procesará tu orden.", Toast.LENGTH_LONG).show()
                }
            },
            enabled = listaCarrito.isNotEmpty(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Confirmar y Enviar Solicitud", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}