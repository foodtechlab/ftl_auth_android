package com.foodtechlab.auth.data.mapper

import com.foodtechlab.auth.data.api.model.response.AuthResponse
import com.foodtechlab.auth.domain.entity.AuthEntity

/**
 * Created by Umalt on 2/25/21
 */
class AuthEntityMapper {
    fun mapToEntity(obj: AuthResponse): AuthEntity {
        return AuthEntity(obj.accessToken, obj.refreshToken)
    }

    fun mapFromEntity(obj: AuthEntity): AuthResponse {
        return AuthResponse(obj.accessToken, obj.refreshToken)
    }
}