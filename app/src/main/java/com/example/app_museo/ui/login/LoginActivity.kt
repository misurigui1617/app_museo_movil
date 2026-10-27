package com.example.app_museo.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.app_museo.ui.main.MainActivity
import com.example.app_museo.R
import com.example.app_museo.data.AppDatabase
import com.example.app_museo.data.Usuario
import com.example.app_museo.ui.registro.RegistroActivity
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etContrasena = findViewById<EditText>(R.id.etLoginContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnDemoCliente = findViewById<Button>(R.id.btnDemoCliente)
        val btnDemoInvestigador = findViewById<Button>(R.id.btnDemoInvestigador)
        val btnDemoAdmin = findViewById<Button>(R.id.btnDemoAdmin)
        val btnIrARegistro = findViewById<Button>(R.id.btnIrARegistro)

        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val dao = db.usuarioDao()

            if (dao.login("admin", "admin") == null) {
                dao.registrarUsuario(
                    Usuario(
                        nombre = "Administrador del Museo",
                        email = "admin",
                        contrasena = "admin",
                        tipoCliente = "Administrador",
                        fotoUri = ""
                    )
                )
            }

            if (dao.login("cliente", "123") == null) {
                dao.registrarUsuario(
                    Usuario(
                        nombre = "Cliente de Prueba",
                        email = "cliente",
                        contrasena = "123",
                        tipoCliente = "Cliente",
                        fotoUri = ""
                    )
                )
            }

            if (dao.login("investigador", "123") == null) {
                dao.registrarUsuario(
                    Usuario(
                        nombre = "Investigador Académico",
                        email = "investigador",
                        contrasena = "123",
                        tipoCliente = "Investigador",
                        fotoUri = ""
                    )
                )
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val usuario = db.usuarioDao().login(email, contrasena)

                    if (usuario != null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "¡Bienvenido, ${usuario.nombre} (${usuario.tipoCliente})!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra("USUARIO_NOMBRE", usuario.nombre)
                            putExtra("USUARIO_ROL", usuario.tipoCliente)
                            putExtra("USUARIO_EMAIL", usuario.email)
                            putExtra("USUARIO_FOTO", usuario.fotoUri)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnDemoAdmin.setOnClickListener {
            etEmail.setText("admin")
            etContrasena.setText("admin")
        }

        btnDemoCliente.setOnClickListener {
            etEmail.setText("cliente")
            etContrasena.setText("123")
        }

        btnDemoInvestigador.setOnClickListener {
            etEmail.setText("investigador")
            etContrasena.setText("123")
        }

        btnIrARegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
