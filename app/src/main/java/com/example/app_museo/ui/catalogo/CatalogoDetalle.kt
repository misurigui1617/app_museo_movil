package com.example.app_museo.ui.catalogo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.app_museo.ui.theme.App_museoTheme

class CatalogoDetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.catalogo_detalle)

        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Curso o Producto Cultural"
        val precio = intent.getStringExtra("EXTRA_PRECIO") ?: "$35.000 CLP"
        val imagenResId = intent.getIntExtra("EXTRA_IMAGEN", R.drawable.cat1)
        val stock = intent.getStringExtra("EXTRA_STOCK") ?: "En stock"

        findViewById<ComposeView>(R.id.composeViewDetalle).setContent {
            App_museoTheme {
                CatalogoDetalleScreen(
                    nombre = nombre,
                    precio = precio,
                    imagenResId = imagenResId,
                    stock = stock,
                    alVolver = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoDetalleScreen(
    nombre: String,
    precio: String,
    imagenResId: Int,
    stock: String,
    alVolver: () -> Unit
) {
    val context = LocalContext.current
    var cantidad by remember { mutableStateOf(1) }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = alVolver) {
                Text(text = "← Volver al Catálogo", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imagenResId),
                    contentDescription = nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = nombre,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A202C)
            )

            Text(
                text = precio,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            Text(
                text = "Descripción y Características",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF2D3748)
            )

            Text(
                text = "Material especializado y de alto valor patrimonial, diseñado para garantizar el estudio riguroso, la conservación adecuada y la difusión cultural de las obras.",
                fontSize = 14.sp,
                color = Color(0xFF4A5568),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Cantidad", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A5568))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .background(Color(0xFFEDF2F7), shape = RoundedCornerShape(10.dp))
                            .padding(4.dp)
                    ) {
                        Button(
                            onClick = { if (cantidad > 1) cantidad-- },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Text(
                            text = cantidad.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Button(
                            onClick = { cantidad++ },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A))
                        ) {
                            Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Disponibilidad", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A5568))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF2E7D32), shape = RoundedCornerShape(50.dp))
                        )
                        Text(text = stock, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (stock.contains("limitado", ignoreCase = true) && cantidad > 3) {
                        Toast.makeText(context, " Error: Stock insuficiente en el servidor para esa cantidad.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, " ¡Solicitud exitosa! $cantidad unidad(es) procesada(s).", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C824A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(text = "Solicitar / Agregar al Carrito", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
