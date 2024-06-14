package com.example.storeslist.data.datasources.remote

import com.example.storeslist.data.network.FrogmiApiService
import com.example.storeslist.domain.mapper.toStore
import com.example.storeslist.data.model.Links
import com.example.storeslist.data.model.Meta
import com.example.storeslist.data.model.Pagination
import com.example.storeslist.data.model.StoreAttributes
import com.example.storeslist.data.model.StoreData
import com.example.storeslist.data.model.StoreResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FrogmiRemoteDataSourceImplTest {

    @MockK
    private lateinit var apiService: FrogmiApiService

    private lateinit var remoteDataSource: FrogmiRemoteDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        remoteDataSource = FrogmiRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getInitialStores should return first stores list page response from api`() = runTest {
        // Given
        val storeDataList = listOf(
            StoreData(id = "1", type = "store", attributes = StoreAttributes(name = "Store 1", code = "1", full_address = "Address 1")),
            StoreData(id = "2", type = "store", attributes = StoreAttributes(name = "Store 2", code = "2", full_address = "Address 2"))
        )
        val response = StoreResponse(
            data = storeDataList,
            meta = Meta(Pagination(current_page = 1, total = 121, per_page = 10)),
            links = Links(prev = null, next = null, first = "first", last = "last", self = "self")
        )
        coEvery { apiService.getStores(any(), any(), 10, 1) } returns response

        // When
        val result = remoteDataSource.getInitialStores()

        // Then
        assertEquals(response, result)
    }

    @Test(expected = Exception::class)
    fun `getInitialStores should return exception when api returns error`() = runTest {
        // Given
        coEvery { apiService.getStores(any(), any(), 10, 1) } throws Exception()

        // When
        remoteDataSource.getInitialStores()
    }
    @Test
    fun `getNextStorePage should return next stores list page response from api`() = runTest {
        // Given
        val storeDataList = listOf(
            StoreData(id = "1", type = "store", attributes = StoreAttributes(name = "Store 1", code = "1", full_address = "Address 1")),
            StoreData(id = "2", type = "store", attributes = StoreAttributes(name = "Store 2", code = "2", full_address = "Address 2"))
        )
        val response = StoreResponse(
            data = storeDataList,
            meta = Meta(Pagination(current_page = 2, total = 121, per_page = 10)),
            links = Links(prev = null, next = null, first = "first", last = "last", self = "self")
        )
        val nextPage = "Url"
        coEvery { apiService.getNextStoresPage(any(), any(), nextPage) } returns response

        // When
        val result = remoteDataSource.getNextStorePage(nextPage)

        // Then
        assertEquals(response, result)
    }

    @Test(expected = Exception::class)
    fun `getNextStorePage should return exception when api returns error`() = runTest {
        // Given
        val nextPage = "Url"
        coEvery { apiService.getNextStoresPage(any(), any(), nextPage) } throws Exception()

        // When
        remoteDataSource.getNextStorePage(nextPage)
    }

}

