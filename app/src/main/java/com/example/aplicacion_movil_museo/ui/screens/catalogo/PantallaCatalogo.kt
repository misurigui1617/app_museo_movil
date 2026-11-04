package com.example.aplicacion_movil_museo.ui.screens.catalogo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.aplicacion_movil_museo.data.models.*
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    onNavegarADetalleProducto: (String) -> Unit
) {
    val currentUser by MuseoRepository.currentUser.collectAsState()
    val catalogItems by MuseoRepository.catalog.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var showAddDialog by remember { mutableStateOf(false) }

    var newTitle by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Cursos") }
    var newPrice by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    val categories = remember(catalogItems) {
        listOf("Todos") + catalogItems.map { it.category }.distinct()
    }

    val filteredCatalog = remember(searchQuery, selectedCategory, catalogItems) {
        catalogItems.filter { item ->
            (selectedCategory == "Todos" || item.category == selectedCategory) &&
                    (searchQuery.isBlank() || item.title.contains(searchQuery, ignoreCase = true) || item.description.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        floatingActionButton = {
            if (currentUser.role == UserRole.ADMIN) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Producto/Curso", tint = Color.White)
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
                text = "Catálogo de Productos y Cursos",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Publicaciones, cursos de formación y materiales de conservación",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar en el catálogo...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCatalog) { item ->
                    FilaElementoCatalogo(
                        item = item,
                        onItemClick = { onNavegarADetalleProducto(item.id) },
                        onAddToCart = {
                            MuseoRepository.addToCart(item)
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Alta de Producto / Curso") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Título") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Categoría (Cursos, Publicaciones, Materiales)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newPrice,
                        onValueChange = { newPrice = it },
                        label = { Text("Precio (CLP)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Descripción") },
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val priceVal = newPrice.toDoubleOrNull() ?: 10000.0
                        val isCourse = newCategory.contains("Curso", ignoreCase = true)
                        MuseoRepository.addProductCourse(
                            ProductCourse(
                                id = "item-${System.currentTimeMillis() % 1000}",
                                title = newTitle.ifBlank { "Nuevo Ítem Museológico" },
                                category = newCategory.ifBlank { "General" },
                                price = priceVal,
                                description = newDescription.ifBlank { "Descripción del recurso" },
                                type = if (isCourse) ItemType.COURSE else ItemType.PRODUCT
                            )
                        )
                        showAddDialog = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun FilaElementoCatalogo(
    item: ProductCourse,
    onItemClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    var addedNotice by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        if (item.type == ItemType.COURSE) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.type == ItemType.COURSE) Icons.Default.School else Icons.Default.Book,
                    contentDescription = null,
                    tint = if (item.type == ItemType.COURSE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(item.category, style = MaterialTheme.typography.labelSmall) }
                )
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "$${item.price.toInt()} CLP",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                )
            }

            IconButton(
                onClick = {
                    onAddToCart()
                    addedNotice = true
                }
            ) {
                Icon(
                    imageVector = if (addedNotice) Icons.Default.Check else Icons.Default.AddShoppingCart,
                    contentDescription = "Agregar al carrito",
                    tint = if (addedNotice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
