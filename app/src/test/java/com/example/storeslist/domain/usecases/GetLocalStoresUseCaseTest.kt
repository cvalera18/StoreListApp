package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.model.Store
import com.example.storeslist.domain.repository.StoreRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLocalStoresUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var storeRepository: StoreRepository

    private lateinit var getLocalStoresUseCase: GetLocalStoresUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getLocalStoresUseCase = GetLocalStoresUseCase(storeRepository)
    }

    @Test
    fun `should return flow of local stores`() = runTest {
        // given
        val expectedStores =
            listOf(Store("1", "Store 1", "Address 1"), Store("2", "Store 2", "Address 2"))
        val expectedFlow: Flow<List<Store>> = flowOf(expectedStores)
        every { storeRepository.localStore } returns expectedFlow

        // when
        val result = getLocalStoresUseCase.invoke()

        // then
        assertEquals(expectedFlow, result)

    }

    @Test
    fun `invoke should emit expected values`() = runTest {
        // given
        val expectedStores =
            listOf(Store("1", "Store 1", "Address 1"), Store("2", "Store 2", "Address 2"))
        val expectedFlow: Flow<List<Store>> = flowOf(expectedStores)
        every { storeRepository.localStore } returns expectedFlow

        // when
        val result = getLocalStoresUseCase.invoke()

        // then
        result.collect { stores ->
            assertEquals(expectedStores, stores)
        }
    }

}