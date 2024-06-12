package com.example.storeslist.data.datasources.local

import com.example.storeslist.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    val allStores: Flow<List<Store>>
}