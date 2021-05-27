package com.example.finaltaskandroid.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().signedRequest()
        return chain.proceed(newRequest)
    }

    private fun Request.signedRequest(): Request {
        return newBuilder()
            .header("Authorization", TOKEN_APP)
            .build()
    }

    companion object {
        const val TOKEN_APP = "4c465727-6fc5-4e6b-b460-c95137b7289f"
    }
}