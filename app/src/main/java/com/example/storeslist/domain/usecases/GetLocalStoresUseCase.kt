package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow

class GetLocalStoresUseCase(
    private val storeRepository: StoreRepository
) {
    fun invoke(): Flow<List<Store>> = storeRepository.localStore
}