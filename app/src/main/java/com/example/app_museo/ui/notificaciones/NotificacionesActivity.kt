package com.example.app_museo.ui.notificaciones

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

data class NotificacionItem(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val destinatarioRol: String
)

class NotificacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.favoritos)

        val rol = intent.getStringExtra("USUARIO_ROL") ?: "Cliente"

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarFavoritos)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Centro de Notificaciones"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewFavoritos).setContent {
            App_museoTheme {
                NotificacionesScreen(rolUsuario = rol)
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

@Composable
fun NotificacionesScreen(rolUsuario: String) {
    val context = LocalContext.current

    val listaNotificaciones = remember {
        mutableStateListOf(
            NotificacionItem(1, "¡Nuevo Curso Disponible!", "Inscripciones abiertas para 'Historia del Arte Iberoamericano'.", "Hoy", "Todos"),
            NotificacionItem(2, "Alerta Museológica", "Nueva ficha técnica agregada a la colección Precolombina.", "Ayer", "Investigador"),
            NotificacionItem(3, "Actualización de Pedido", "Tu solicitud SOL-102 ha cambiado a estado Confirmado.", "22/10", "Cliente")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (rolUsuario == "Administrador") {
            Button(
                onClick = {
                    listaNotificaciones.add(
                        0, NotificacionItem(
                            id = listaNotificaciones.size + 1,
                            titulo = "Aviso Push de Prueba",
                            mensaje = "Notificación generada por el Administrador.",
                            fecha = "Ahora",
                            destinatarioRol = "Todos"
                        )
                    )
                    Toast.makeText(context, "Notificación enviada a los usuarios", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(" Enviar Notificación de Prueba (Admin)", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(listaNotificaciones) { notif ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = notif.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A202C))
                            Text(text = notif.fecha, fontSize = 11.sp, color = Color.Gray)
                        }
                        Text(text = notif.mensaje, fontSize = 13.sp, color = Color(0xFF4A5568))
                    }
                }
            }
        }
    }
}