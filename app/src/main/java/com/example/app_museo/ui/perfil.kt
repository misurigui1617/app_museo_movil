package com.example.app_museo.ui.perfil

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.favoritos)

        val nombre = intent.getStringExtra("USUARIO_NOMBRE") ?: "Usuario Museo"
        val rol = intent.getStringExtra("USUARIO_ROL") ?: "Cliente"

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarFavoritos)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Perfil y Configuración"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ComposeView>(R.id.composeViewFavoritos).setContent {
            App_museoTheme {
                PerfilScreen(nombreInicial = nombre, rolInicial = rol)
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
fun PerfilScreen(nombreInicial: String, rolInicial: String) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(nombreInicial) }
    var permisoCamara by remember { mutableStateOf(true) }
    var permisoUbicacion by remember { mutableStateOf(true) }
    var notificacionesActivas by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF9C824A), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = nombre.take(1).uppercase(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                Column {
                    Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1A202C))
                    Text(text = "Rol: $rolInicial", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Datos Personales", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (nombre.isBlank()) {
                            Toast.makeText(context, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Perfil guardado en SQLite", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF2FFFFFF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Ajustes y Permisos", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Permiso de Cámara (Lector QR)")
                    Switch(checked = permisoCamara, onCheckedChange = { permisoCamara = it })
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Permiso de Ubicación (Mapa)")
                    Switch(checked = permisoUbicacion, onCheckedChange = { permisoUbicacion = it })
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Notificaciones Push")
                    Switch(checked = notificacionesActivas, onCheckedChange = { notificacionesActivas = it })
                }
            }
        }


        Button(
            onClick = {
                Toast.makeText(context, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
                (context as? AppCompatActivity)?.finish()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}