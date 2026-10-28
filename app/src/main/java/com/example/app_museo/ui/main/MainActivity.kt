package com.example.app_museo.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.app_museo.R
import com.example.app_museo.ui.carrito.CarritoActivity
import com.example.app_museo.ui.catalogo.CatalogoActivity
import com.example.app_museo.ui.mapa.MapaActivity
import com.example.app_museo.ui.qr.QrActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private var nombreUsuario = "Usuario"
    private var rolUsuario = "Cliente"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val tvNombre = findViewById<TextView>(R.id.tvMainNombre)
        val tvRol = findViewById<TextView>(R.id.tvMainRol)
        val imgPerfil = findViewById<ImageView>(R.id.imgMainPerfil)

        nombreUsuario = intent.getStringExtra("USUARIO_NOMBRE") ?: "Usuario"
        rolUsuario = intent.getStringExtra("USUARIO_ROL") ?: "Cliente"
        val fotoUri = intent.getStringExtra("USUARIO_FOTO")

        tvNombre?.text = nombreUsuario
        tvRol?.text = "Rol: $rolUsuario"

        if (!fotoUri.isNullOrEmpty()) {
            try {
                if (fotoUri.startsWith("content://")) {
                    imgPerfil?.setImageURI(Uri.parse(fotoUri))
                } else {
                    val file = java.io.File(fotoUri)
                    if (file.exists()) {
                        imgPerfil?.setImageURI(Uri.fromFile(file))
                    } else {
                        imgPerfil?.setImageResource(R.drawable.ic_launcher_foreground)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                imgPerfil?.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarHeader)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        // Configurar botón hamburguesa en el Toolbar para abrir el menú lateral
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar Clics en las Cards del Centro
        findViewById<MaterialCardView>(R.id.cardCarrito).setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardMapa).setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardFavoritos).setOnClickListener {
            startActivity(Intent(this, com.example.app_museo.ui.favorito.FavoritosActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardQr).setOnClickListener {
            startActivity(Intent(this, QrActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardSolicitudes).setOnClickListener {
            val intent = Intent(this, com.example.app_museo.ui.solicitudes.SolicitudesActivity::class.java).apply {
                putExtra("USUARIO_ROL", rolUsuario)
            }
            startActivity(intent)
        }

        findViewById<MaterialCardView>(R.id.cardNotificaciones).setOnClickListener {
            val intent = Intent(this, com.example.app_museo.ui.notificaciones.NotificacionesActivity::class.java).apply {
                putExtra("USUARIO_ROL", rolUsuario)
            }
            startActivity(intent)
        }

        findViewById<MaterialCardView>(R.id.cardPerfil).setOnClickListener {
            val intent = Intent(this, com.example.app_museo.ui.perfil.PerfilActivity::class.java).apply {
                putExtra("USUARIO_NOMBRE", nombreUsuario)
                putExtra("USUARIO_ROL", rolUsuario)
            }
            startActivity(intent)
        }

        // Configurar Menú Inferior Mejorado
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> true
                R.id.nav_mapa -> {
                    startActivity(Intent(this, MapaActivity::class.java))
                    true
                }
                R.id.nav_catalogo -> {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    true
                }
                R.id.nav_notificaciones -> {
                    val intent = Intent(this, com.example.app_museo.ui.notificaciones.NotificacionesActivity::class.java).apply {
                        putExtra("USUARIO_ROL", rolUsuario)
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_perfil -> {
                    val intent = Intent(this, com.example.app_museo.ui.perfil.PerfilActivity::class.java).apply {
                        putExtra("USUARIO_NOMBRE", nombreUsuario)
                        putExtra("USUARIO_ROL", rolUsuario)
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    // Manejar opciones del Menú Lateral (Drawer)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_drawer_inicio -> {
                // Ya estamos en inicio
            }
            R.id.nav_drawer_catalogo -> {
                startActivity(Intent(this, CarritoActivity::class.java))
            }
            R.id.nav_drawer_mapa -> {
                startActivity(Intent(this, MapaActivity::class.java))
            }
            R.id.nav_drawer_favoritos -> {
                startActivity(Intent(this, com.example.app_museo.ui.favorito.FavoritosActivity::class.java))
            }
            R.id.nav_drawer_qr -> {
                startActivity(Intent(this, QrActivity::class.java))
            }
            R.id.nav_drawer_perfil -> {
                val intent = Intent(this, com.example.app_museo.ui.perfil.PerfilActivity::class.java).apply {
                    putExtra("USUARIO_NOMBRE", nombreUsuario)
                    putExtra("USUARIO_ROL", rolUsuario)
                }
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}