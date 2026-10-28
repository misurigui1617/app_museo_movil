package com.example.app_museo.ui.solicitudes

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class SolicitudItem(
    val id: String,
    val concepto: String,
    val fecha: String,
    val monto: Int,
    var estado: String
)

class SolicitudesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.favoritos)

        val rol = intent.getStringExtra("USUARIO_ROL") ?: "Cliente"

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarFavoritos)
        setSupportActionBar(toolbar)
        supportActionBar?.title = if (rol == "Administrador") "Gestión de Solicitudes" else "Mis Solicitudes y Compras"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewFavoritos).setContent {
            App_museoTheme {
                SolicitudesScreen(rolUsuario = rol)
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
fun SolicitudesScreen(rolUsuario: String) {
    val context = LocalContext.current

    val listaSolicitudes = remember {
        mutableStateListOf(
            SolicitudItem("SOL-101", "Curso de Curaduría Virtual", "24/10/2026", 45000, "Pendiente"),
            SolicitudItem("SOL-102", "Réplica Escultura Precolombina", "22/10/2026", 28000, "Confirmado"),
            SolicitudItem("SOL-103", "Entrada General + Taller Arte", "18/10/2026", 12000, "Entregado")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (rolUsuario == "Administrador") "Panel de Control de Pedidos" else "Historial de Transacciones",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(listaSolicitudes, key = { it.id }) { solicitud ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = solicitud.id, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A202C))

                            val colorEstado = when (solicitud.estado) {
                                "Pendiente" -> Color(0xFFFF9800)
                                "Confirmado" -> Color(0xFF2196F3)
                                "Enviado" -> Color(0xFF9C27B0)
                                else -> Color(0xFF4CAF50)
                            }

                            Surface(
                                color = colorEstado.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = solicitud.estado,
                                    color = colorEstado,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(text = solicitud.concepto, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        Text(text = "Fecha: ${solicitud.fecha} • Total: $${solicitud.monto} CLP", fontSize = 12.sp, color = Color.Gray)

                        // Si es Administrador, permite cambiar el estado del pedido en tiempo real
                        if (rolUsuario == "Administrador") {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Text(text = "Cambiar Estado (Admin):", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("Pendiente", "Confirmado", "Enviado", "Entregado").forEach { nuevoEstado ->
                                    OutlinedButton(
                                        onClick = {
                                            val index = listaSolicitudes.indexOf(solicitud)
                                            listaSolicitudes[index] = solicitud.copy(estado = nuevoEstado)
                                            Toast.makeText(context, "Estado de ${solicitud.id} actualizado a $nuevoEstado", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(nuevoEstado, fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}