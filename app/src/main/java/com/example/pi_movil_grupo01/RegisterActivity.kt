package com.example.pi_movil_grupo01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.Insets
import com.example.pi_movil_grupo01.entity.AuthResponse
import com.example.pi_movil_grupo01.entity.RegisterRequest
import com.example.pi_movil_grupo01.service.AuthService
import com.example.pi_movil_grupo01.util.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tcGoLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tcGoLogin = findViewById(R.id.tv_go_to_login)

        etPassword.addTextChangedListener(passwordWatcher)
        etConfirmPassword.addTextChangedListener(confirmPasswordWatcher)
        etEmail.addTextChangedListener(emailWatcher)

        btnRegister.setOnClickListener {
            register()
        }

        tcGoLogin.setOnClickListener {
            goToLogin()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validatePassword()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private val confirmPasswordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                etConfirmPassword.error = "Las contraseñas no coinciden"
            } else {
                etConfirmPassword.error = null
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private val emailWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                etEmail.error = "Correo electrónico no válido"
            } else {
                etEmail.error = null
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validatePassword() {
        val password = etPassword.text.toString()

        // Expresión regular para al menos un número y un carácter especial
        val hasNumber = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        when {
            password.length < 6 -> {
                etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            }
            !hasNumber -> {
                etPassword.error = "La contraseña debe tener al menos un número"
            }
            !hasSpecialChar -> {
                etPassword.error = "La contraseña debe tener al menos un carácter especial"
            }
            else -> {
                etPassword.error = null
            }
        }
    }

    private fun register() {
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val request = RegisterRequest(nombre, apellido, email, password)

        val service = ApiClient.getClient(this).create(AuthService::class.java)
        service.register(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    goToLogin()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
