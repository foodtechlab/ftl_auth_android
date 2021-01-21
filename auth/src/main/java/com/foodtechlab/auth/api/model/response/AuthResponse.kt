package com.foodtechlab.auth.api.model.response

/**
 * Created by Umalt on 1/14/21
 */
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)
