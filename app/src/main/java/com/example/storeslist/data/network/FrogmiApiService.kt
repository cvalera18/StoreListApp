package com.example.storeslist.data.network

import com.example.storeslist.domain.model.StoreResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FrogmiApiService {
    @GET("stores")
    suspend fun getStores(
        @Header("Authorization") token: String,
        @Header("X-Company-UUID") companyId: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): StoreResponse
}