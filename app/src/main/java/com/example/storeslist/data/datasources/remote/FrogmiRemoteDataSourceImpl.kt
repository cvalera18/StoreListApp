package com.example.storeslist.data.datasources.remote

import com.example.storeslist.domain.mapper.toStore
import com.example.storeslist.domain.model.Store
import com.example.storeslist.data.network.FrogmiApiService
import com.example.storeslist.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class FrogmiRemoteDataSourceImpl @Inject constructor(
    private val apiService: FrogmiApiService
) : FrogmiRemoteDataSource {

    override suspend fun getStores(perPage: Int, page: Int): List<Store> {
        val token = BuildConfig.BEARER_TOKEN
        val companyId = BuildConfig.COMPANY_UUID
        val response = apiService.getStores("Bearer $token", companyId, perPage, page)
        return response.data.map { it.toStore() }
    }
}
