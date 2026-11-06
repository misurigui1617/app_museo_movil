package com.example.aplicacion_movil_museo.ui.screens.qr

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEscanerQr(
    onQrEscaneadoExito: (String) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    var manualCodeInput by remember { mutableStateOf("") }
    var mensajeEstado by remember { mutableStateOf<String?>(null) }
    var esError by remember { mutableStateOf(false) }

    val orangeSoft = Color(0xFFFF9E43)

    val infiniteTransition = rememberInfiniteTransition(label = "qr_laser")
    val laserOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    fun iniciarEscaneoCamara() {
        try {
            val opciones = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            GmsBarcodeScanning.getClient(context, opciones)
                .startScan()
                .addOnSuccessListener { barcode ->
                    val rawValue = barcode.rawValue ?: ""
                    if (rawValue.isNotBlank()) {
                        val record = MuseoRepository.processQrCode(rawValue)
                        if (record != null) {
                            onQrEscaneadoExito(record.code)
                        } else {
                            onQrEscaneadoExito(rawValue) // Navega indicando "QR rechazado o no encontrado"
                        }
                    } else {
                        mensajeEstado = "El código QR leído está vacío."
                        esError = true
                    }
                }
                .addOnCanceledListener {
                    mensajeEstado = "Escaneo cancelado por el usuario."
                    esError = false
                }
                .addOnFailureListener { e ->
                    mensajeEstado = "Aviso: Usa el modo de prueba o revisa Google Play Services (${e.localizedMessage})"
                    esError = true
                }
        } catch (ex: Exception) {
            mensajeEstado = "Iniciando modo simulador/manual (${ex.localizedMessage})"
            esError = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escáner de Código QR", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Lector de Códigos QR Museológicos",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = orangeSoft
            )

            Text(
                text = "Escanea el código QR de un museo o publicación para consultar su ficha e inscribirlo en tu historial.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .background(Color.Black, RoundedCornerShape(24.dp))
                    .border(3.dp, orangeSoft, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.size(130.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(2.dp)
                        .offset(y = (laserOffsetY - 90).dp)
                        .background(orangeSoft)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { iniciarEscaneoCamara() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = orangeSoft,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Abrir Cámara y Escanear QR", fontWeight = FontWeight.Bold)
            }

            mensajeEstado?.let { msg ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = msg,
                    color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Códigos de Prueba Rápida:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = {
                                val record = MuseoRepository.processQrCode("MMDH-CHILE")
                                record?.let { onQrEscaneadoExito(it.code) }
                            },
                            label = { Text("QR Museo MMDH") },
                            leadingIcon = { Icon(Icons.Default.Museum, contentDescription = null) }
                        )

                        AssistChip(
                            onClick = {
                                val record = MuseoRepository.processQrCode("MUSEO-M3")
                                record?.let { onQrEscaneadoExito(it.code) }
                            },
                            label = { Text("QR Bellas Artes") },
                            leadingIcon = { Icon(Icons.Default.Museum, contentDescription = null) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = manualCodeInput,
                        onValueChange = { manualCodeInput = it; mensajeEstado = null },
                        label = { Text("Ingresar Código Manualmente") },
                        leadingIcon = { Icon(Icons.Default.Keyboard, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            if (manualCodeInput.isBlank()) {
                                mensajeEstado = "Ingresa un código QR válido."
                                esError = true
                            } else {
                                val record = MuseoRepository.processQrCode(manualCodeInput)
                                if (record != null) {
                                    onQrEscaneadoExito(record.code)
                                } else {
                                    onQrEscaneadoExito(manualCodeInput)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Procesar Código Manual")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
