package com.example.pi_movil_grupo01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import androidx.core.graphics.Insets
import com.example.pi_movil_grupo01.util.PreferenceHelper

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val preferences: SharedPreferences = PreferenceHelper.defaultPrefs(this)
        if (!preferences.getBoolean("session", false)) {
            goToLogin()
            return
        }

        val btnLogout: Button = findViewById(R.id.btn_logout)
        btnLogout.setOnClickListener {
            logout()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun logout() {
        val preferences: SharedPreferences = PreferenceHelper.defaultPrefs(this)
        preferences.edit()
            .remove("auth_token")
            .remove("session")
            .apply()

        goToLogin()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}