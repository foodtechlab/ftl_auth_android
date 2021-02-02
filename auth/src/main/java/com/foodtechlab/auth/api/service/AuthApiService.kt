package com.foodtechlab.auth.api.service

import com.foodtechlab.auth.api.model.request.AuthRequest
import com.foodtechlab.auth.api.model.request.RefreshRequest
import com.foodtechlab.auth.api.model.response.AuthResponse
import com.foodtechlab.auth.api.model.response.BaseResponse
import com.foodtechlab.auth.api.model.response.TimerResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Umalt on 1/14/21
 */
interface AuthApiService {

    @POST("{apiVersion}/auth/init/sms")
    suspend fun authSms(
        @Path(value = "apiVersion", encoded = true) apiVersion: String,
        @Body body: AuthRequest
    ): BaseResponse<TimerResponse>

    @POST("{apiVersion}/auth/login/sms")
    suspend fun loginSms(
        @Path(value = "apiVersion", encoded = true) apiVersion: String,
        @Body body: AuthRequest
    ): BaseResponse<AuthResponse>

    @POST("{apiVersion}/auth/login/password")
    suspend fun loginPassword(
        @Path(value = "apiVersion", encoded = true) apiVersion: String,
        @Body body: AuthRequest
    ): BaseResponse<AuthResponse>

    @POST("{apiVersion}/auth/refresh")
    suspend fun refresh(
        @Path(value = "apiVersion", encoded = true) apiVersion: String,
        @Body body: RefreshRequest
    ): BaseResponse<AuthResponse>

    @POST("{apiVersion}/auth/logout")
    suspend fun logout(
        @Path(value = "apiVersion", encoded = true) apiVersion: String
    ): BaseResponse<String>
}