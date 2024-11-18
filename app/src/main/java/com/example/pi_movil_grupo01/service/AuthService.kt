package com.example.pi_movil_grupo01.service

import com.example.pi_movil_grupo01.entity.AuthRequest
import com.example.pi_movil_grupo01.entity.AuthResponse
import com.example.pi_movil_grupo01.entity.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<AuthResponse>
}