package com.example.app_museo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.app_museo.R
import com.example.app_museo.ui.mapa.MapaActivity
import com.example.app_museo.ui.qr.QrActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.osmdroid.config.Configuration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Configuration.getInstance().load(this, prefs)
        Configuration.getInstance().userAgentValue = "AppMuseoChile/1.0"
        Configuration.getInstance().save(this, prefs)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarHeader)
        setSupportActionBar(toolbar)

        val tvNombre = findViewById<TextView>(R.id.tvMainNombre)
        val tvRol = findViewById<TextView>(R.id.tvMainRol)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val seccionCliente = findViewById<LinearLayout>(R.id.seccionCliente)
        val seccionAdmin = findViewById<LinearLayout>(R.id.seccionAdmin)
        val seccionInvestigador = findViewById<LinearLayout>(R.id.seccionInvestigador)

        val nombre = intent.getStringExtra("USUARIO_NOMBRE") ?: "Usuario"
        val rol = intent.getStringExtra("USUARIO_ROL") ?: "Cliente"

        tvNombre.text = nombre
        tvRol.text = "Rol: $rol"

        when (rol) {
            "Administrador" -> {
                seccionAdmin?.visibility = View.VISIBLE
                seccionCliente?.visibility = View.GONE
                seccionInvestigador?.visibility = View.GONE
            }
            "Investigador" -> {
                seccionInvestigador?.visibility = View.VISIBLE
                seccionCliente?.visibility = View.GONE
                seccionAdmin?.visibility = View.GONE
            }
            else -> {
                seccionCliente?.visibility = View.VISIBLE
                seccionAdmin?.visibility = View.GONE
                seccionInvestigador?.visibility = View.GONE
            }
        }

        bottomNav?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> true
                R.id.nav_mapa -> {
                    val intent = Intent(this, MapaActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_catalogo -> {
                    val intent = Intent(this, com.example.app_museo.ui.catalogo.CatalogoActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_notificaciones -> true
                R.id.nav_perfil -> true
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_qr_scanner -> {
                val intent = Intent(this, QrActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
