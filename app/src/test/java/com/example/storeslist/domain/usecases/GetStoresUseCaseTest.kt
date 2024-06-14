package com.example.storeslist.domain.usecases

import com.example.storeslist.domain.repository.StoreRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetStoresUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var storeRepository: StoreRepository

    private lateinit var getStoresUseCase: GetStoresUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getStoresUseCase = GetStoresUseCase(storeRepository)
    }

    @Test
    fun `invoke calls getStores from repository`() = runTest {
        // given
        coEvery { storeRepository.getStores() } returns Unit

        //when
        getStoresUseCase()

        //then
        coVerify(exactly = 1) { storeRepository.getStores() }
    }

    @Test(expected = Exception::class)
    fun `when repository throws error then use case throws error`() = runTest {
        // given
        coEvery { storeRepository.getStores() } throws Exception()
        //when
        getStoresUseCase()
    }
}