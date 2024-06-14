package com.example.storeslist.data.datasources.remote

import com.example.storeslist.domain.mapper.toStore
import com.example.storeslist.domain.model.Store
import com.example.storeslist.data.network.FrogmiApiService
import com.example.storeslist.BuildConfig
import com.example.storeslist.data.model.StoreResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class FrogmiRemoteDataSourceImpl @Inject constructor(
    private val apiService: FrogmiApiService
) : FrogmiRemoteDataSource {

    private val token = BuildConfig.BEARER_TOKEN
    private val companyId = BuildConfig.COMPANY_UUID

    override suspend fun getInitialStores(perPage: Int, page: Int): StoreResponse {
        val response = apiService.getStores("Bearer $token", companyId, perPage, page)
        return response
    }

    override suspend fun getNextStorePage(nextPage: String): StoreResponse {
        return apiService.getNextStoresPage("Bearer $token", companyId, nextPage)
    }
}
