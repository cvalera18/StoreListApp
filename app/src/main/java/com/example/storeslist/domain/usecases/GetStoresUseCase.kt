package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.repository.StoreRepository

class GetStoresUseCase(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke() {
        storeRepository.getStores()
    }
}