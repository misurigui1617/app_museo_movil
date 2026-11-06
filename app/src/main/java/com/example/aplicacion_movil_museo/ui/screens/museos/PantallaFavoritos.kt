package com.example.aplicacion_movil_museo.ui.screens.museos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacion_movil_museo.data.models.ItemType
import com.example.aplicacion_movil_museo.data.models.Museum
import com.example.aplicacion_movil_museo.data.models.ProductCourse
import com.example.aplicacion_movil_museo.data.repository.MuseoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos(
    onNavegarADetalleMuseo: (String) -> Unit,
    onNavegarADetalleProducto: (String) -> Unit = {}
) {
    val currentUser by MuseoRepository.currentUser.collectAsState()
    var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0: Museos, 1: Catálogo
    var searchQuery by remember { mutableStateOf("") }

    var favMuseums by remember(currentUser) { mutableStateOf(MuseoRepository.getFavoriteMuseums()) }
    var favProducts by remember(currentUser) { mutableStateOf(MuseoRepository.getFavoriteProducts()) }

    val filteredMuseums = remember(favMuseums, searchQuery) {
        if (searchQuery.isBlank()) favMuseums
        else favMuseums.filter { it.name.contains(searchQuery, ignoreCase = true) || it.country.contains(searchQuery, ignoreCase = true) }
    }

    val filteredProducts = remember(favProducts, searchQuery) {
        if (searchQuery.isBlank()) favProducts
        else favProducts.filter { it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Colección de Favoritos",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Guarda y gestiona tus museos, publicaciones y cursos favoritos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pestañas de Filtro: "Museos" y "Catálogo"
        TabRow(
            selectedTabIndex = selectedCategoryTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedCategoryTab == 0,
                onClick = { selectedCategoryTab = 0 },
                text = { Text("Museos (${favMuseums.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Museum, contentDescription = null) }
            )
            Tab(
                selected = selectedCategoryTab == 1,
                onClick = { selectedCategoryTab = 1 },
                text = { Text("Catálogo (${favProducts.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Storefront, contentDescription = null) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar en mis favoritos...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCategoryTab == 0) {
            // Categoría: MUSEOS
            if (filteredMuseums.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tienes museos guardados en favoritos",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredMuseums) { museum ->
                        TarjetaMuseoFavorito(
                            museum = museum,
                            onDetailClick = { onNavegarADetalleMuseo(museum.id) },
                            onRemoveClick = {
                                MuseoRepository.toggleFavorite(museum.id)
                                favMuseums = MuseoRepository.getFavoriteMuseums()
                            }
                        )
                    }
                }
            }
        } else {
            // Categoría: CATÁLOGO (Cursos / Productos)
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tienes productos o cursos guardados en favoritos",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredProducts) { product ->
                        TarjetaProductoFavorito(
                            product = product,
                            onDetailClick = { onNavegarADetalleProducto(product.id) },
                            onRemoveClick = {
                                MuseoRepository.toggleProductFavorite(product.id)
                                favProducts = MuseoRepository.getFavoriteProducts()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaMuseoFavorito(
    museum: Museum,
    onDetailClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Museum,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = museum.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${museum.city}, ${museum.country}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = museum.classification,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun TarjetaProductoFavorito(
    product: ProductCourse,
    onDetailClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (product.type == ItemType.COURSE) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (product.imageResId != 0) {
                    Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = if (product.type == ItemType.COURSE) Icons.Default.School else Icons.Default.Book,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Stock: ${product.stock} unidades",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$${product.price.toInt()} CLP",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
