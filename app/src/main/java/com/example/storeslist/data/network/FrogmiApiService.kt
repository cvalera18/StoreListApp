package com.example.storeslist.data.network

import com.example.storeslist.data.model.StoreResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface FrogmiApiService {
    @GET("stores")
    suspend fun getStores(
        @Header("Authorization") token: String,
        @Header("X-Company-UUID") companyId: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): StoreResponse

    @GET
    suspend fun getNextStoresPage(
        @Header("Authorization") token: String,
        @Header("X-Company-UUID") companyId: String,
        @Url nextPageUrl: String
    ): StoreResponse
}