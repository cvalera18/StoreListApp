package com.example.storeslist.domain.repository

import com.example.storeslist.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getStores(page: Int): Flow<List<Store>>
}