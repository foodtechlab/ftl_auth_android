package com.foodtechlab.auth.data.api.model.request

/**
 * Created by Umalt on 1/14/21
 */
data class AuthRequest(
    val email: String? = null,
    val phoneNumber: String? = null,
    val code: String? = null,
    val password: String? = null,
)
