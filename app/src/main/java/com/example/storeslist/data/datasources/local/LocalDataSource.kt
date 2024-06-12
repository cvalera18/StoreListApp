package com.example.storeslist.data.datasources.local

import com.example.storeslist.data.models.Store
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    val allStores: Flow<List<Store>>
}