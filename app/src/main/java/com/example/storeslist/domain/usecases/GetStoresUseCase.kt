package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke() {
        storeRepository.getStores()
    }
}

class GetLocalStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    fun invoke(): Flow<List<Store>> = storeRepository.localStore
}
