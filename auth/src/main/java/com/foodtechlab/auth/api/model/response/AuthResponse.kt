package com.foodtechlab.auth.api.model.response

/**
 * Created by Umalt on 1/14/21
 */
data class AuthResponse(
    val accessToken: Token,
    val refreshToken: Token
)

data class Token(
    val token: String,
    val timeZone: String,
    val expiredAt: Long
)
