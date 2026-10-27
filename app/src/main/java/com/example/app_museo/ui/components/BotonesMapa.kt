package com.example.app_museo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app_museo.R
import com.example.app_museo.ui.theme.App_museoTheme

@Composable
fun BotonesMapa(
    alAcercar: () -> Unit,
    alAlejar: () -> Unit,
    alIrAMiUbicacion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SmallFloatingActionButton(
            onClick = alAcercar,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Acercar")
        }
        SmallFloatingActionButton(
            onClick = alAlejar,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Icon(
                painterResource(R.drawable.ic_menos),
                contentDescription = "Alejar",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        FloatingActionButton(
            onClick = alIrAMiUbicacion,
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                painterResource(R.drawable.ic_mi_ubicacion),
                contentDescription = "Mi ubicación",
                tint = Color.White,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BotonesMapaPreview() {
    App_museoTheme { BotonesMapa(alAcercar = {}, alAlejar = {}, alIrAMiUbicacion = {}) }
}
