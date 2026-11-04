package com.example.aplicacion_movil_museo.ui.screens.compras

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.aplicacion_movil_museo.data.models.RequestOrder
import com.example.aplicacion_movil_museo.data.models.RequestStatus
import com.example.aplicacion_movil_museo.data.models.UserRole
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorialSolicitudes() {
    val currentUser by MuseoRepository.currentUser.collectAsState()
    val orders by MuseoRepository.orders.collectAsState()

    val displayOrders = remember(orders, currentUser) {
        if (currentUser.role == UserRole.ADMIN) orders
        else orders.filter { it.userId == currentUser.id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (currentUser.role == UserRole.ADMIN) "Gestión de Solicitudes (Admin)" else "Historial de Mis Solicitudes",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Consulta de pedidos, fechas, montos y estado de tramitación",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (displayOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No hay solicitudes registradas", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(displayOrders) { order ->
                    TarjetaHistorialOrden(
                        order = order,
                        isAdmin = currentUser.role == UserRole.ADMIN,
                        onStatusChange = { newStatus ->
                            MuseoRepository.updateOrderStatus(order.id, newStatus)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaHistorialOrden(
    order: RequestOrder,
    isAdmin: Boolean,
    onStatusChange: (RequestStatus) -> Unit
) {
    var expandedStatusMenu by remember { mutableStateOf(false) }

    val statusColor = when (order.status) {
        RequestStatus.PENDING -> Color(0xFFFF9800)
        RequestStatus.CONFIRMED -> Color(0xFF2196F3)
        RequestStatus.SHIPPED -> Color(0xFF9C27B0)
        RequestStatus.DELIVERED -> Color(0xFF4CAF50)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Orden: ${order.id}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = order.status.label,
                        color = statusColor,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Fecha: ${order.date}", style = MaterialTheme.typography.bodySmall)
            Text("Dirección: ${order.shippingAddress}", style = MaterialTheme.typography.bodySmall)
            Text("Total: $${order.totalAmount.toInt()} CLP", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))

            Spacer(modifier = Modifier.height(8.dp))

            Text("Ítems solicitados:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            order.items.forEach { cartItem ->
                Text("• ${cartItem.productCourse.title} (x${cartItem.quantity})", style = MaterialTheme.typography.bodySmall)
            }

            if (isAdmin) {
                Spacer(modifier = Modifier.height(12.dp))
                Box {
                    OutlinedButton(
                        onClick = { expandedStatusMenu = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cambiar Estado (Admin)")
                    }

                    DropdownMenu(
                        expanded = expandedStatusMenu,
                        onDismissRequest = { expandedStatusMenu = false }
                    ) {
                        RequestStatus.entries.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    onStatusChange(st)
                                    expandedStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
