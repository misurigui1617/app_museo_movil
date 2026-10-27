package com.example.app_museo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_museo.Lugar
import com.example.app_museo.ui.theme.App_museoTheme
import com.example.app_museo.ui.theme.RojoMarcador
import org.osmdroid.util.GeoPoint

@Composable
fun BarraBusqueda(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    alBuscar: () -> Unit,
    buscando: Boolean,
    resultados: List<Lugar>,
    alElegir: (Lugar) -> Unit,
    alLimpiar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val foco = LocalFocusManager.current

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
    ) {
        TextField(
            value = texto,
            onValueChange = alCambiarTexto,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar un lugar…") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (buscando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else if (texto.isNotEmpty()) {
                    IconButton(onClick = alLimpiar) {
                        Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                foco.clearFocus()
                alBuscar()
            }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )

        resultados.forEach { lugar ->
            HorizontalDivider()
            FilaResultado(
                lugar = lugar,
                alTocar = {
                    foco.clearFocus()
                    alElegir(lugar)
                },
            )
        }
    }
}

@Composable
private fun FilaResultado(lugar: Lugar, alTocar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alTocar)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Place, contentDescription = null, tint = RojoMarcador)
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = lugar.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = lugar.direccion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BarraBusquedaPreview() {
    App_museoTheme {
        BarraBusqueda(
            texto = "Torre Eiffel",
            alCambiarTexto = {},
            alBuscar = {},
            buscando = false,
            resultados = listOf(
                Lugar(nombre = "Torre Eiffel", direccion = "Av. Gustave Eiffel, 75007 París, Francia", punto = GeoPoint(48.8584, 2.2945)),
            ),
            alElegir = {},
            alLimpiar = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
