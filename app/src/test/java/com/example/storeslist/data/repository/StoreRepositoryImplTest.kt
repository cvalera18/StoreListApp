package com.example.storeslist.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storeslist.MainDispatcherRule
import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.model.Store
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class StoreRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var remoteDataSource: FrogmiRemoteDataSource

    @RelaxedMockK
    private lateinit var localDataSource: LocalDataSource

    @RelaxedMockK
    private lateinit var networkUtils: NetworkUtils

    private lateinit var storeRepository: StoreRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        storeRepository = StoreRepositoryImpl(remoteDataSource, localDataSource, networkUtils)
    }

    @Test
    fun `getStores should fetch from remote and save to local when online`() = runTest {
        // given
        val perPage = 10
        val page = 1
        val remoteStores = listOf(
            Store("ST001", "Store 1", "123 Main St"),
            Store("ST002", "Store 2", "456 Main St")
        )

        coEvery { networkUtils.isInternetAvailable() } returns true
//        coEvery { remoteDataSource.getStores(perPage, page) } returns remoteStores

        // when
        val result = storeRepository.getStores(page).toList()

        // then
        coVerify { localDataSource.saveStores(remoteStores) }
        assertEquals(remoteStores, result.first())
    }

    @Test
    fun `getStores should fetch from local when offline`() = runTest {
        // given
        val perPage = 10
        val page = 1
        val localStores = listOf(
            Store("ST001", "Store 1", "123 Main St"),
            Store("ST002", "Store 2", "456 Main St")
        )

        coEvery { networkUtils.isInternetAvailable() } returns false
        coEvery { localDataSource.getStores() } returns flowOf(localStores)

        // when
        val result = storeRepository.getStores(page).toList()

        // then
//        coVerify(exactly = 0) { remoteDataSource.getStores(any(), any()) }
        assertEquals(localStores, result.first())
    }

    @Test
    fun `getStores should handle errors from remote source gracefully`() = runTest {
        // given
        val perPage = 10
        val page = 1
        val localStores = listOf(
            Store("ST001", "Store 1", "123 Main St"),
            Store("ST002", "Store 2", "456 Main St")
        )

        coEvery { networkUtils.isInternetAvailable() } returns true
//        coEvery { remoteDataSource.getStores(perPage, page) } throws IOException("Remote source error")
        coEvery { localDataSource.getStores() } returns flowOf(localStores)

        // when
        val result = storeRepository.getStores(page).first()

        // then
//        coVerify { remoteDataSource.getStores(perPage, page) }
        coVerify { localDataSource.getStores() }
        assertEquals(localStores, result)
    }
}