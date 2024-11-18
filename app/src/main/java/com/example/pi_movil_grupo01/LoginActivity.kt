package com.example.pi_movil_grupo01

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi_movil_grupo01.entity.AuthRequest
import com.example.pi_movil_grupo01.entity.AuthResponse
import com.example.pi_movil_grupo01.service.AuthService
import com.example.pi_movil_grupo01.util.ApiClient
import com.example.pi_movil_grupo01.util.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tcGoRegister: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btn_go_to_menu)
        tcGoRegister = findViewById(R.id.tv_go_to_register)

        val preferences: SharedPreferences = PreferenceHelper.defaultPrefs(this)
        if (preferences.getBoolean("session", false)) {
            goToMenu()
        }

        btnLogin.setOnClickListener {
            login()
        }

        tcGoRegister.setOnClickListener {
            goToRegister()
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tus credenciales", Toast.LENGTH_SHORT).show()
            return
        }

        val request = AuthRequest(email, password)

        val service = ApiClient.getClient(this).create(AuthService::class.java)
        service.login(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token

                    PreferenceHelper.defaultPrefs(this@LoginActivity).edit()
                        .putString("auth_token", token)
                        .putBoolean("session", true)
                        .apply()

                    Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                    goToMenu()
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}