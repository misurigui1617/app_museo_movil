package com.example.app_museo.ui.registro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.app_museo.R
import com.example.app_museo.data.AppDatabase
import com.example.app_museo.data.Usuario
import com.example.app_museo.ui.login.LoginActivity
import kotlinx.coroutines.launch

class RegistroActivity : ComponentActivity() {

    private var imagenUri: String = ""
    private lateinit var imgPerfil: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnSeleccionarFoto = findViewById<Button>(R.id.btnSeleccionarFoto)
        imgPerfil = findViewById(R.id.imgPerfil)

        val db = AppDatabase.getDatabase(this)

        val selectorGaleria = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val file = java.io.File(filesDir, "perfil_${System.currentTimeMillis()}.jpg")
                    inputStream?.use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    imagenUri = file.absolutePath
                    imgPerfil.setImageURI(Uri.fromFile(file))
                } catch (e: Exception) {
                    e.printStackTrace()
                    imagenUri = uri.toString()
                    imgPerfil.setImageURI(uri)
                }
            }
        }

        btnSeleccionarFoto.setOnClickListener {
            selectorGaleria.launch("image/*")
        }

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val nuevoUsuario = Usuario(
                        nombre = nombre,
                        email = email,
                        contrasena = contrasena,
                        tipoCliente = "Cliente",
                        fotoUri = imagenUri
                    )
                    db.usuarioDao().registrarUsuario(nuevoUsuario)

                    Toast.makeText(this@RegistroActivity, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
