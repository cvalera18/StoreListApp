package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStoresUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    operator fun invoke(perPage: Int, page: Int): Flow<List<Store>> {
        return storeRepository.getStores(perPage, page)
    }
}
