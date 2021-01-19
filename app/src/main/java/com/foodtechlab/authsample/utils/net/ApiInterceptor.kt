package com.foodtechlab.authsample.utils.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Umalt on 1/19/21
 */
class ApiInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder().apply {
//            userService.accessToken?.let {
//                header("X-Auth-Token", it)
//            }
//            header("X-Device-Type", "ANDROID")
//            firebaseNotificationService.firebaseToken?.let {
//                header("X-Device-Token", it)
//            }
//            header("Content-Type", "application/json")
//            header("X-Application-Version-Code", getVersionCode(context))
//            header(
//                "X-Application-Version",
//                context.packageManager.getPackageInfo(context.packageName, 0).versionName
//            )
            method(originalRequest.method(), originalRequest.body())
        }

        return try {
            chain.proceed(requestBuilder.build())
        } catch (e: Exception) {
            throw e
        }
    }
}