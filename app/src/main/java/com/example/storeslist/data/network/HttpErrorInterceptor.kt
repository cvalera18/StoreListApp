package com.example.storeslist.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HttpErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            val errorMessage = when (response.code) {
                400 -> "Bad Request: $errorBody"
                401 -> "Unauthorized: $errorBody"
                403 -> "Forbidden: $errorBody"
                404 -> "Not Found: $errorBody"
                500 -> "Internal Server Error: $errorBody"
                502 -> "Bad Gateway: $errorBody"
                503 -> "Service Unavailable: $errorBody"
                504 -> "Gateway Timeout: $errorBody"
                in 400..499 -> "Client error: ${response.message}"
                in 500..599 -> "Server error: ${response.message}"
                else -> "Unknown error: ${response.message}"
            }
            throw IOException(errorMessage)
        }
        return response
    }
}

