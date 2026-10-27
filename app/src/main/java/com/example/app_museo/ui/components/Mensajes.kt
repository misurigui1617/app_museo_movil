package com.example.app_museo.ui.components

import android.content.Context
import android.widget.Toast

fun Context.mostrarMensaje(texto: String) {
    Toast.makeText(this, texto, Toast.LENGTH_SHORT).show()
}
