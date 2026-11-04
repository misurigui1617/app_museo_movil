package com.example.aplicacion_movil_museo.ui.screens.principal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.models.NotificationItem
import com.example.aplicacion_movil_museo.data.models.UserRole
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNotificaciones(
    onNavegarARuta: (String) -> Unit
) {
    val currentUser by MuseoRepository.currentUser.collectAsState()
    val notifications by MuseoRepository.notifications.collectAsState()

    var showSendNoticeDialog by remember { mutableStateOf(false) }
    var noticeTitle by remember { mutableStateOf("") }
    var noticeMessage by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            if (currentUser.role == UserRole.ADMIN) {
                FloatingActionButton(
                    onClick = { showSendNoticeDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Campaign, contentDescription = "Enviar Aviso Push")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Centro de Notificaciones",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Novedades, avisos de cursos y alertas de estado de tus solicitudes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tienes notificaciones por el momento")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(notifications) { notif ->
                        TarjetaNotificacion(
                            item = notif,
                            onClick = {
                                MuseoRepository.markNotificationRead(notif.id)
                                notif.targetScreenRoute?.let { route -> onNavegarARuta(route) }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showSendNoticeDialog) {
        AlertDialog(
            onDismissRequest = { showSendNoticeDialog = false },
            title = { Text("Enviar Notificación Push de Prueba") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = noticeTitle,
                        onValueChange = { noticeTitle = it },
                        label = { Text("Título del Aviso") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = noticeMessage,
                        onValueChange = { noticeMessage = it },
                        label = { Text("Mensaje Informativo") },
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noticeTitle.isNotBlank()) {
                            MuseoRepository.addNotification(noticeTitle, noticeMessage)
                        }
                        showSendNoticeDialog = false
                    }
                ) {
                    Text("Enviar Notificación")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSendNoticeDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaNotificacion(
    item: NotificationItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isRead) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (!item.isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = item.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
