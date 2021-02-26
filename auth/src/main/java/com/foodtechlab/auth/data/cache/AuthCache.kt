package com.foodtechlab.auth.data.cache

/**
 * Created by Umalt on 1/14/21
 */
interface AuthCache {

    fun saveAccessToken(token: String?)

    fun saveRefreshToken(token: String?)

    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun isAuthCompleted(): Boolean

    fun clear()
}