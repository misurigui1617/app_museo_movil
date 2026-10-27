package com.example.app_museo

import android.app.Application
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration

class MuseoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Configuration.getInstance().load(this, prefs)

        Configuration.getInstance().userAgentValue = "AppMuseoChile/1.0 (contacto@appmuseo.com)"
        Configuration.getInstance().save(this, prefs)

        try {
            val cacheDir = Configuration.getInstance().osmdroidTileCache
            if (cacheDir != null && cacheDir.exists()) {
                cacheDir.deleteRecursively()
            }
            cacheDir?.resolve("osmdroid")?.deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
