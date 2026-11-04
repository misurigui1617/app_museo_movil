package com.example.aplicacion_movil_museo.ui.screens.qr

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEscanerQr(
    onQrEscaneadoExito: (String) -> Unit,
    onVolver: () -> Unit
) {
    var manualCodeInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "qr_laser")
    val laserOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escáner de Código QR") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Apunta al Código QR del Museo o Producto",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Color.Black, RoundedCornerShape(24.dp))
                    .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(140.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(2.dp)
                        .offset(y = (laserOffsetY - 100).dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Códigos de Prueba Rápida (Simulador):",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {
                            val record = MuseoRepository.processQrCode("MUSEO-M1")
                            onQrEscaneadoExito(record.code)
                        },
                        label = { Text("QR Museo Prado") },
                        leadingIcon = { Icon(Icons.Default.Museum, contentDescription = null) }
                    )

                    AssistChip(
                        onClick = {
                            val record = MuseoRepository.processQrCode("PUB-P2")
                            onQrEscaneadoExito(record.code)
                        },
                        label = { Text("QR Anuario") },
                        leadingIcon = { Icon(Icons.Default.MenuBook, contentDescription = null) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = manualCodeInput,
                    onValueChange = { manualCodeInput = it; errorMessage = null },
                    label = { Text("Ingresar Código Manualmente") },
                    leadingIcon = { Icon(Icons.Default.Keyboard, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (manualCodeInput.isBlank()) {
                            errorMessage = "Ingresa un código QR válido."
                        } else {
                            val record = MuseoRepository.processQrCode(manualCodeInput)
                            onQrEscaneadoExito(record.code)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Procesar Código QR")
                }
            }
        }
    }
}
