package com.example.aplicacion_movil_museo.ui.screens.museos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.models.UserRole
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleMuseo(
    museumId: String,
    onVolver: () -> Unit
) {
    val currentUser by MuseoRepository.currentUser.collectAsState()
    val museums by MuseoRepository.museums.collectAsState()
    val museum = museums.find { it.id == museumId } ?: museums.firstOrNull()

    var editDescription by remember(museum) { mutableStateOf(museum?.description ?: "") }
    var isEditing by remember { mutableStateOf(false) }
    var isPublished by remember { mutableStateOf(true) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    if (museum == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Museo no encontrado")
        }
        return
    }

    val isFav = MuseoRepository.isFavorite(museum.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(museum.name) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { MuseoRepository.toggleFavorite(museum.id) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Museum,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = museum.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(
                    onClick = { },
                    label = { Text(museum.classification) },
                    icon = { Icon(Icons.Default.Category, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                SuggestionChip(
                    onClick = { },
                    label = { Text("${museum.city}, ${museum.country}") },
                    icon = { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Descripción y Reseña Cultural",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing && (currentUser.role == UserRole.INVESTIGATOR || currentUser.role == UserRole.ADMIN)) {
                OutlinedTextField(
                    value = editDescription,
                    onValueChange = { editDescription = it },
                    label = { Text("Editar Reseña de Investigación") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isEditing = false
                        showSuccessMessage = true
                    }
                ) {
                    Text("Guardar Cambios de Ficha")
                }
            } else {
                Text(
                    text = editDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (showSuccessMessage) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ficha museológica actualizada correctamente.",
                    color = MaterialTheme.colorScheme.primary,
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
                        text = "Recorrido Virtual 360°",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Enlace oficial a la experiencia inmersiva digital.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar Visita Virtual")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (currentUser.role) {
                UserRole.INVESTIGATOR -> {
                    Text(
                        text = "Acciones de Investigador",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { isEditing = !isEditing },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isEditing) "Cancelar Edición" else "Corregir / Modificar Ficha")
                    }
                }

                UserRole.ADMIN -> {
                    Text(
                        text = "Gestión de Publicación (Admin)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Estado de Publicación: ${if (isPublished) "Publicado" else "Borrador"}")
                        Switch(
                            checked = isPublished,
                            onCheckedChange = { isPublished = it }
                        )
                    }
                }

                UserRole.CLIENT -> {}
            }
        }
    }
}
