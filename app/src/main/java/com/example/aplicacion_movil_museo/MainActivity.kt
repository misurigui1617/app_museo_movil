package com.example.aplicacion_movil_museo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aplicacion_movil_museo.ui.navigation.AppNavigation
import com.example.aplicacion_movil_museo.ui.theme.Aplicacion_movil_museoTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración global de OSMDroid antes de renderizar la UI
        Configuration.getInstance().apply {
            userAgentValue = "MuseoDeLosMuseos/1.0 (Android; ${android.os.Build.MODEL})"
            load(applicationContext, getSharedPreferences("osmdroid_pref", MODE_PRIVATE))
        }

        enableEdgeToEdge()
        setContent {
            Aplicacion_movil_museoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
