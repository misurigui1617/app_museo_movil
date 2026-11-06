package com.example.aplicacion_movil_museo.ui.screens.compras

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.models.CartItem
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(
    onNavegarAHistorial: () -> Unit,
    onNavegarACatalogo: () -> Unit
) {
    val cartItems by MuseoRepository.cart.collectAsState()
    var isInternational by remember { mutableStateOf(false) }
    var shippingAddress by remember { mutableStateOf("") }

    // Estados para Pago con Tarjeta de Débito o Crédito
    var cardHolderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var orderSubmittedSuccess by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.productCourse.price * it.quantity }
    val totalItemsCount = cartItems.sumOf { it.quantity }
    val shippingCost = MuseoRepository.calculateShipping(isInternational, totalItemsCount)
    val totalFinal = subtotal + shippingCost

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito y Proceso de Pago", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        if (orderSubmittedSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Pago y Solicitud Confirmados!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Tu orden fue procesada exitosamente con tarjeta de débito/crédito.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavegarAHistorial,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ver en Historial de Solicitudes")
                    }
                }
            }
        } else if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "El carrito de compras está vacío",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onNavegarACatalogo,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Explorar Catálogo")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Artículos Seleccionados (${cartItems.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                cartItems.forEach { item ->
                    FilaItemCarrito(item = item)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN 1: ESTIMACIÓN DE ENVÍO ---
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Dirección y Costo de Envío",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Destino Internacional:")
                            Switch(
                                checked = isInternational,
                                onCheckedChange = { isInternational = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = shippingAddress,
                            onValueChange = { shippingAddress = it; errorMessage = null },
                            label = { Text("Dirección de Envío / Ciudad") },
                            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN 2: PAGO CON TARJETA DE DÉBITO O CRÉDITO ---
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pago con Tarjeta de Débito / Crédito",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = cardHolderName,
                            onValueChange = { cardHolderName = it; errorMessage = null },
                            label = { Text("Nombre del Titular") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { if (it.length <= 16) cardNumber = it; errorMessage = null },
                            label = { Text("Número de Tarjeta (16 dígitos)") },
                            leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = cardExpiry,
                                onValueChange = { if (it.length <= 5) cardExpiry = it; errorMessage = null },
                                label = { Text("Expiración (MM/AA)") },
                                placeholder = { Text("12/28") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = cardCvv,
                                onValueChange = { if (it.length <= 4) cardCvv = it; errorMessage = null },
                                label = { Text("CVV") },
                                placeholder = { Text("123") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- DESGLOSE FINAL DE PRECIO ---
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal Productos:")
                            Text("$${subtotal.toInt()} CLP", fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Envío (${if (isInternational) "Internacional" else "Nacional"}):")
                            Text("$${shippingCost.toInt()} CLP", fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total a Pagar:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("$${totalFinal.toInt()} CLP", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                        }
                    }
                }

                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            shippingAddress.isBlank() -> {
                                errorMessage = "Por favor ingresa la dirección de envío."
                            }
                            cardHolderName.isBlank() || cardNumber.length < 16 || cardExpiry.isBlank() || cardCvv.length < 3 -> {
                                errorMessage = "Por favor ingresa todos los datos válidos de la tarjeta (Titular, 16 dígitos, MM/AA y CVV)."
                            }
                            else -> {
                                val order = MuseoRepository.submitOrder(shippingAddress, isInternational)
                                if (order != null) {
                                    orderSubmittedSuccess = true
                                } else {
                                    errorMessage = "No se pudo procesar la solicitud."
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pagar y Confirmar Solicitud ($${totalFinal.toInt()} CLP)")
                }
            }
        }
    }
}

@Composable
private fun FilaItemCarrito(item: CartItem) {
    Card(shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productCourse.title, fontWeight = FontWeight.Bold)
                Text(
                    "$${item.productCourse.price.toInt()} CLP c/u",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { MuseoRepository.updateCartQuantity(item.productCourse.id, item.quantity - 1) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Menos")
                }
                Text("${item.quantity}", fontWeight = FontWeight.Bold)
                IconButton(onClick = { MuseoRepository.updateCartQuantity(item.productCourse.id, item.quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Más")
                }
            }
        }
    }
}
