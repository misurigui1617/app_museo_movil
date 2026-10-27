package com.example.app_museo.ui.bienvenida

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.example.app_museo.R
import com.example.app_museo.ui.login.LoginActivity

class BienvenidaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bienvenida)

        val imgLogo = findViewById<ImageView>(R.id.imgLogoAnimado)
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        val animacion = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animacion.duration = 1000
        imgLogo.startAnimation(animacion)

        btnComenzar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
