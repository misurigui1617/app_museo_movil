package com.example.app_museo.ui.qr

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_museo.R
import com.example.app_museo.data.Video
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import org.json.JSONObject

@Composable
fun QrScreen() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    var videoLeido by remember { mutableStateOf<Video?>(null) }
    var mensajeEstado by remember { mutableStateOf("Presiona el botón para escanear un código QR") }
    var escaneoExitoso by remember { mutableStateOf(false) }

    fun escanearQr() {
        val scanner = GmsBarcodeScanning.getClient(context)
        scanner.startScan()
            .addOnSuccessListener { barcode: Barcode ->
                val rawValue: String = barcode.rawValue ?: ""
                val video = parsearJsonQr(rawValue)

                if (video != null) {
                    videoLeido = video
                    mensajeEstado = "¡Escaneo exitoso!"
                } else {
                    videoLeido = Video(
                        titulo = "Museo de la Memoria",
                        descripcion = "El Museo de la Memoria y los Derechos Humanos es un espacio en Santiago dedicado a dignificar a las víctimas y visibilizar las violaciones a los derechos humanos cometidas en Chile entre 1973 y 1990.",
                        url = if (rawValue.startsWith("http")) rawValue else "",
                        imagenResId = R.drawable.cat1
                    )
                    mensajeEstado = "QR leído correctamente"
                }
                escaneoExitoso = true
            }
            .addOnCanceledListener {
                mensajeEstado = "Escaneo cancelado"
            }
            .addOnFailureListener { e: Exception ->
                mensajeEstado = "Error al escanear: ${e.localizedMessage}"
                Toast.makeText(context, "Error de lectura QR", Toast.LENGTH_SHORT).show()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!escaneoExitoso) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color(0xFFFF9800).copy(alpha = 0.1f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Escáner de Códigos QR",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212529)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mensajeEstado,
                    fontSize = 14.sp,
                    color = Color(0xFF6C757D),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { escanearQr() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(text = "Escanear código QR", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .background(Color(0xFF00BFA5), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Éxito",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Código identificado!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212529)
                )

                Text(
                    text = "Hemos encontrado la siguiente información:",
                    fontSize = 14.sp,
                    color = Color(0xFF6C757D),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                videoLeido?.let { video ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE2E8F0))
                            ) {
                                Image(
                                    painter = painterResource(id = video.imagenResId),
                                    contentDescription = video.titulo,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE2E8F0)
                            ) {
                                Text(
                                    text = "Registro Museo",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4A5568)
                                )
                            }

                            Text(
                                text = video.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A202C)
                            )

                            Text(
                                text = video.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF718096)
                            )
                        }
                    }

                    if (video.url.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (video.url.startsWith("http://") || video.url.startsWith("https://")) {
                                    uriHandler.openUri(video.url)
                                } else {
                                    Toast.makeText(context, "URL inválida", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(text = "Ver detalle / Enlace", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        escaneoExitoso = false
                        videoLeido = null
                        mensajeEstado = "Presiona el botón para escanear un código QR"
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, tint = Color(0xFF212529))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Volver a escanear", fontSize = 15.sp, color = Color(0xFF212529))
                }
            }
        }
    }
}

fun parsearJsonQr(jsonStr: String): Video? {
    return try {
        val json = JSONObject(jsonStr)
        Video(
            titulo = json.optString("titulo", "Museo de la Memoria"),
            descripcion = json.optString("descripcion", "El Museo de la Memoria y los Derechos Humanos es un espacio en Santiago dedicado a dignificar a las víctimas y visibilizar las violaciones a los derechos humanos cometidas en Chile entre 1973 y 1990."),
            url = json.optString("url", ""),
            imagenResId = R.drawable.museofotoqr
        )
    } catch (e: Exception) {
        Video(
            titulo = "Museo de la Memoria",
            descripcion = "El Museo de la Memoria y los Derechos Humanos es un espacio en Santiago dedicado a dignificar a las víctimas y visibilizar las violaciones a los derechos humanos cometidas en Chile entre 1973 y 1990.",
            url = if (jsonStr.startsWith("http")) jsonStr else "",
            imagenResId = R.drawable.museofotoqr
        )
    }
}
