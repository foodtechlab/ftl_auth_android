package com.foodtechlab.auth.data.api.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Umalt on 1/14/21
 */
object AuthApiServiceFactory {

    fun makeAuthApiService(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}