package com.example.storeslist.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storeslist.MainDispatcherRule
import com.example.storeslist.data.datasources.local.LocalDataSource
import com.example.storeslist.data.datasources.remote.FrogmiRemoteDataSource
import com.example.storeslist.data.model.Links
import com.example.storeslist.data.model.Meta
import com.example.storeslist.data.model.Pagination
import com.example.storeslist.data.model.StoreAttributes
import com.example.storeslist.data.model.StoreData
import com.example.storeslist.data.model.StoreResponse
import com.example.storeslist.data.network.NetworkUtils
import com.example.storeslist.domain.mapper.toStore
import com.example.storeslist.domain.model.Store
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoreRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var remoteDataSource: FrogmiRemoteDataSource

    @RelaxedMockK
    private lateinit var localDataSource: LocalDataSource

    @MockK
    private lateinit var networkUtils: NetworkUtils

    private lateinit var storeRepository: StoreRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        storeRepository = StoreRepositoryImpl(remoteDataSource, localDataSource, networkUtils)
    }

    @Test
    fun `getStores should call getInitialStores from remoteDataSource`() = runTest {
        // given
        every { networkUtils.isInternetAvailable() } returns true

        val storeDataList = listOf(
            StoreData(
                id = "1",
                type = "store",
                attributes = StoreAttributes(
                    name = "Store 1",
                    code = "1",
                    full_address = "Address 1"
                )
            ),
            StoreData(
                id = "2",
                type = "store",
                attributes = StoreAttributes(
                    name = "Store 2",
                    code = "2",
                    full_address = "Address 2"
                )
            )
        )
        val response = StoreResponse(
            data = storeDataList,
            meta = Meta(Pagination(current_page = 1, total = 121, per_page = 10)),
            links = Links(prev = null, next = null, first = "first", last = "last", self = "self")
        )
        coEvery { remoteDataSource.getInitialStores() } returns response

        // when
        storeRepository.getStores()

        // then
        coVerify { remoteDataSource.getInitialStores() }
    }

    @Test
    fun `When getInitialStores is successful, saveStores in localDataSource should be called`() =
        runTest {
            // given
            every { networkUtils.isInternetAvailable() } returns true

            val storeDataList = listOf(
                StoreData(
                    id = "1",
                    type = "store",
                    attributes = StoreAttributes(
                        name = "Store 1",
                        code = "1",
                        full_address = "Address 1"
                    )
                ),
                StoreData(
                    id = "2",
                    type = "store",
                    attributes = StoreAttributes(
                        name = "Store 2",
                        code = "2",
                        full_address = "Address 2"
                    )
                )
            )
            val response = StoreResponse(
                data = storeDataList,
                meta = Meta(Pagination(current_page = 1, total = 121, per_page = 10)),
                links = Links(
                    prev = null,
                    next = null,
                    first = "first",
                    last = "last",
                    self = "self"
                )
            )
            val storeList = response.data.map { it.toStore() }
            coEvery { remoteDataSource.getInitialStores() } returns response

            // when
            storeRepository.getStores()

            // then
            coVerify { localDataSource.saveStores(storeList) }
        }

    @Test
    fun `getStores should call getNextStorePage from remoteDataSource`() = runTest {
        // given
        every { networkUtils.isInternetAvailable() } returns true

        val storeDataList = listOf(
            StoreData(
                id = "1",
                type = "store",
                attributes = StoreAttributes(
                    name = "Store 1",
                    code = "1",
                    full_address = "Address 1"
                )
            ),
            StoreData(
                id = "2",
                type = "store",
                attributes = StoreAttributes(
                    name = "Store 2",
                    code = "2",
                    full_address = "Address 2"
                )
            )
        )
        val nextPage = "NextPageUrl"
        val response = StoreResponse(
            data = storeDataList,
            meta = Meta(Pagination(current_page = 1, total = 121, per_page = 10)),
            links = Links(
                prev = null,
                next = nextPage,
                first = "first",
                last = "last",
                self = "self"
            )
        )
        coEvery { remoteDataSource.getInitialStores() } returns response
        coEvery { remoteDataSource.getNextStorePage(nextPage) } returns response

        // when
        storeRepository.getStores()
        storeRepository.getStores()

        // then
        coVerify { remoteDataSource.getNextStorePage(nextPage) }
    }

    @Test
    fun `When getNextStorePage is successful, saveStores in localDataSource should be called`() =
        runTest {
            // given
            every { networkUtils.isInternetAvailable() } returns true

            val storeDataList = listOf(
                StoreData(
                    id = "1",
                    type = "store",
                    attributes = StoreAttributes(
                        name = "Store 1",
                        code = "1",
                        full_address = "Address 1"
                    )
                ),
                StoreData(
                    id = "2",
                    type = "store",
                    attributes = StoreAttributes(
                        name = "Store 2",
                        code = "2",
                        full_address = "Address 2"
                    )
                )
            )
            val nextPage = "NextPageUrl"
            val response = StoreResponse(
                data = storeDataList,
                meta = Meta(Pagination(current_page = 1, total = 121, per_page = 10)),
                links = Links(
                    prev = null,
                    next = nextPage,
                    first = "first",
                    last = "last",
                    self = "self"
                )
            )
            val storeList = response.data.map { it.toStore() }
            coEvery { remoteDataSource.getInitialStores() } returns response
            coEvery { remoteDataSource.getNextStorePage(nextPage) } returns response

            // when
            storeRepository.getStores()
            storeRepository.getStores()

            // then
            coVerify { localDataSource.saveStores(storeList) }
        }

    @Test
    fun `when nextPage is null, then don't call remoteDataSource`() =
        runTest {
            // given
            every { networkUtils.isInternetAvailable() } returns true
            val storeDataList = listOf(
                StoreData(
                    id = "1",
                    type = "store",
                    attributes = StoreAttributes(
                        name = "Store 1",
                        code = "1",
                        full_address = "Address 1"
                    )
                ),
                StoreData(
                    id = "2",
                    type = "store",
                    attributes = StoreAttributes(
                        name = "Store 2",
                        code = "2",
                        full_address = "Address 2"
                    )
                )
            )

            val response = StoreResponse(
                data = storeDataList,
                meta = Meta(Pagination(current_page = 1, total = 121, per_page = 10)),
                links = Links(
                    prev = null,
                    next = null,
                    first = "first",
                    last = "last",
                    self = "self"
                )
            )
            val storeList = response.data.map { it.toStore() }
            coEvery { remoteDataSource.getInitialStores() } returns response

            // when
            storeRepository.getStores()
            storeRepository.getStores()

            // then
            coVerify(exactly = 1) { remoteDataSource.getInitialStores() }
            coVerify(exactly = 0) { remoteDataSource.getNextStorePage(any()) }
            coVerify(exactly = 1) { localDataSource.saveStores(storeList) }
        }

    @Test(expected = Exception::class)
    fun `when remote data source throw an error, then propagate the error`() =
        runTest {
            // given
            every { networkUtils.isInternetAvailable() } returns true
            val exception = Exception("Error")
            coEvery { remoteDataSource.getInitialStores() } throws exception
            // then
            storeRepository.getStores()
        }

    @Test(expected = Exception::class)
    fun `when Internet is not available, then propagate the error`() =
        runTest {
            // given
            every { networkUtils.isInternetAvailable() } returns false
            // then
            storeRepository.getStores()
        }
}