package com.example.app_museo.ui.carrito

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarCarrito)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Carrito y Pago"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewCarrito).setContent {
            App_museoTheme {
                CarritoScreen(alVolver = { finish() })
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
fun CarritoScreen(alVolver: () -> Unit) {
    val context = LocalContext.current

    val listaCarrito = remember {
        mutableStateListOf(
            ItemCarrito(1, "Curso de Curaduría Virtual", 45000, 1),
            ItemCarrito(2, "Historia del Arte Iberoamericano", 32000, 2)
        )
    }

    var destinoSeleccionado by remember { mutableStateOf("Nacional (Chile)") }
    var modalidadEnvio by remember { mutableStateOf("Estándar") }

    var mostrarFormularioPago by remember { mutableStateOf(false) }
    var pagoAprobado by remember { mutableStateOf(false) }

    var tipoTarjeta by remember { mutableStateOf("Tarjeta de Crédito") }
    var nombreTitular by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaExpiracion by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val subtotal = listaCarrito.sumOf { it.precioUnitario * it.cantidad }
    val costoEnvio = when {
        listaCarrito.isEmpty() -> 0
        destinoSeleccionado == "Nacional (Chile)" -> if (modalidadEnvio == "Estándar") 4500 else 8000
        else -> if (modalidadEnvio == "Estándar") 18000 else 32000
    }
    val totalFinal = subtotal + costoEnvio

    if (pagoAprobado) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn() + fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFF4CAF50), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Éxito",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡Pago Aprobado!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu transacción por $$totalFinal CLP ha sido procesada con éxito.",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = alVolver,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Volver al Inicio", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    } else {
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
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "El carrito está vacío", color = Color.Gray, fontSize = 14.sp)
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
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                }

                                // Controles Interactivos (+ y -)
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
                                        Text("-", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }

                                    Text(
                                        text = "${item.cantidad}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp)
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
                                        Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            Text(text = "Opciones de Envío", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = destinoSeleccionado == "Nacional (Chile)",
                    onClick = { destinoSeleccionado = "Nacional (Chile)" },
                    label = { Text("Nacional") }
                )
                FilterChip(
                    selected = destinoSeleccionado == "Internacional",
                    onClick = { destinoSeleccionado = "Internacional" },
                    label = { Text("Internacional") }
                )
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal:")
                        Text("$${subtotal} CLP", fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Envío:")
                        Text("$${costoEnvio} CLP", fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total a Pagar:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("$${totalFinal} CLP", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            }

            if (mostrarFormularioPago) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Datos de la Tarjeta", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = tipoTarjeta == "Tarjeta de Crédito",
                                onClick = { tipoTarjeta = "Tarjeta de Crédito" },
                                label = { Text("Crédito") }
                            )
                            FilterChip(
                                selected = tipoTarjeta == "Tarjeta de Débito",
                                onClick = { tipoTarjeta = "Tarjeta de Débito" },
                                label = { Text("Débito") }
                            )
                        }

                        OutlinedTextField(
                            value = nombreTitular,
                            onValueChange = { nombreTitular = it },
                            label = { Text("Nombre del Titular") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = numeroTarjeta,
                            onValueChange = { if (it.length <= 16) numeroTarjeta = it },
                            label = { Text("Número de Tarjeta (16 dígitos)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = fechaExpiracion,
                                onValueChange = { if (it.length <= 5) fechaExpiracion = it },
                                label = { Text("MM/AA") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { if (it.length <= 4) cvv = it },
                                label = { Text("CVV") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Button(
                            onClick = {
                                if (nombreTitular.isBlank() || numeroTarjeta.length < 15 || cvv.isBlank()) {
                                    Toast.makeText(context, "Por favor completa los datos de la tarjeta", Toast.LENGTH_SHORT).show()
                                } else {
                                    pagoAprobado = true
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Pagar $$totalFinal CLP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            } else {
                Button(
                    onClick = {
                        if (listaCarrito.isEmpty()) {
                            Toast.makeText(context, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                        } else {
                            mostrarFormularioPago = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Proceder al Pago con Tarjeta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}