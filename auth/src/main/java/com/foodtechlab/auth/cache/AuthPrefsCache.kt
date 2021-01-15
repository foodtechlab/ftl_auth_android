package com.foodtechlab.auth.cache

import android.content.SharedPreferences
import com.foodtechlab.auth.api.model.response.Token
import com.squareup.moshi.Moshi

/**
 * Created by Umalt on 1/14/21
 */
class AuthPrefsCache(private val prefs: SharedPreferences) : IAuthCache {

    private val authCacheModel by lazy {
        prefs.getString(KEY_TOKENS, null)?.let {
            tokenJsonAdapter.fromJson(it)
        } ?: AuthCacheModel()
    }

    private val moshi = Moshi.Builder().build()

    private val tokenJsonAdapter = moshi.adapter(AuthCacheModel::class.java)

    override fun saveAccessToken(token: Token) {
        authCacheModel.accessToken = token
        prefs.edit()
            .putString(KEY_TOKENS, tokenJsonAdapter.toJson(authCacheModel))
            .apply()
    }

    override fun saveRefreshToken(token: Token) {
        authCacheModel.refreshToken = token
        prefs.edit()
            .putString(KEY_TOKENS, tokenJsonAdapter.toJson(authCacheModel))
            .apply()
    }

    override fun getAccessToken(): Token? {
        return authCacheModel.accessToken
    }

    override fun getRefreshToken(): Token?{
        return authCacheModel.refreshToken
    }

    override fun isAuthCompleted(): Boolean {
        return authCacheModel.accessToken != null && authCacheModel.refreshToken != null
    }

    override fun clear() {
        with(authCacheModel) {
            accessToken = null
            refreshToken = null
        }
        prefs.edit()
            .remove(KEY_TOKENS)
            .apply()
    }
}

data class AuthCacheModel(
    var accessToken: Token? = null,
    var refreshToken: Token? = null
)