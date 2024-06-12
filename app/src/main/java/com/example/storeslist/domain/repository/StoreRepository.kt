package com.example.storeslist.domain.repository

import com.example.storeslist.data.models.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    val allStores: Flow<List<Store>>
}