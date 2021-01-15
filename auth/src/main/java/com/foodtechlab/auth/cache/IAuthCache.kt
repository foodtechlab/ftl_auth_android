package com.foodtechlab.auth.cache

import com.foodtechlab.auth.api.model.response.Token

/**
 * Created by Umalt on 1/14/21
 */
interface IAuthCache {

    fun saveAccessToken(token: Token)

    fun saveRefreshToken(token: Token)

    fun getAccessToken(): Token?

    fun getRefreshToken(): Token?

    fun isAuthCompleted(): Boolean

    fun clear()
}