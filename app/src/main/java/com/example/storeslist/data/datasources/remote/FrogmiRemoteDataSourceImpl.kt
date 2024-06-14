package com.example.storeslist.data.datasources.remote

import com.example.storeslist.BuildConfig
import com.example.storeslist.data.model.StoreResponse
import com.example.storeslist.data.network.FrogmiApiService

private const val PAGE_SIZE = 10
private const val FIRST_PAGE = 1
class FrogmiRemoteDataSourceImpl(
    private val apiService: FrogmiApiService
) : FrogmiRemoteDataSource {

    private val token = BuildConfig.BEARER_TOKEN
    private val companyId = BuildConfig.COMPANY_UUID

    override suspend fun getInitialStores(): StoreResponse {
        val response = apiService.getStores("Bearer $token", companyId, PAGE_SIZE, FIRST_PAGE)
        return response
    }

    override suspend fun getNextStorePage(nextPage: String): StoreResponse {
        return apiService.getNextStoresPage("Bearer $token", companyId, nextPage)
    }
}
