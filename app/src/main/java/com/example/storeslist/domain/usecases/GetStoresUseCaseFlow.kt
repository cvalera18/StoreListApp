package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoresUseCaseFlow @Inject constructor(
    private val storeRepository: StoreRepository
) {
    operator fun invoke() = storeRepository.allStores
}