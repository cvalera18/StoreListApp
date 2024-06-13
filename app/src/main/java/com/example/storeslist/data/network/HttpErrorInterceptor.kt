package com.example.storeslist.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HttpErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                val errorMessage = when (response.code) {
                    400 -> "Error 400: Bad Request: $errorBody"
                    401 -> "Error 401: Unauthorized: $errorBody"
                    403 -> "Error 403: Forbidden: $errorBody"
                    404 -> "Error 404: Not Found: $errorBody"
                    500 -> "Error 500: Internal Server Error: $errorBody"
                    502 -> "Error 502: Bad Gateway: $errorBody"
                    503 -> "Error 503: Service Unavailable: $errorBody"
                    504 -> "Error 504: Gateway Timeout: $errorBody"
                    in 400..499 -> "Client error ${response.code}: ${response.message} - $errorBody"
                    in 500..599 -> "Server error ${response.code}: ${response.message} - $errorBody"
                    else -> "Unknown error ${response.code}: ${response.message} - $errorBody"
                }
                throw IOException(errorMessage)
            }
            response
        } catch (e: IOException) {
            throw IOException("Network error: ${e.message}", e)
        }
    }
}
