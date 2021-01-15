package com.foodtechlab.auth.api.service

import com.foodtechlab.auth.api.model.request.AuthRequest
import com.foodtechlab.auth.api.model.request.RefreshRequest
import com.foodtechlab.auth.api.model.response.AuthResponse
import com.foodtechlab.auth.api.model.response.BaseResponse
import com.foodtechlab.auth.api.model.response.TimerResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Umalt on 1/14/21
 */
interface AuthApiService {

    @POST("/auth/init/sms")
    suspend fun authSms(@Body body: AuthRequest): BaseResponse<TimerResponse>

    @POST("/auth/login/sms")
    suspend fun loginSms(@Body body: AuthRequest): BaseResponse<AuthResponse>

    @POST("/auth/login/password")
    suspend fun loginPassword(@Body body: AuthRequest): BaseResponse<AuthResponse>

    @POST("/auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): BaseResponse<AuthResponse>

    @POST("/auth/logout")
    suspend fun logout(): BaseResponse<String>
}