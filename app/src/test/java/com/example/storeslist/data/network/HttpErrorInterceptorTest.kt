package com.example.storeslist.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException


class HttpErrorInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpErrorInterceptor())
            .build()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `intercept should throw IOException for 400 response`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(400).setBody("Bad Request"))

        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .build()

        try {
            okHttpClient.newCall(request).execute()
            Assert.fail("Exception was not thrown")
        } catch (e: IOException) {
            Assert.assertTrue(e.message!!.contains("Bad Request: Bad Request"))
        }
    }

    @Test
    fun `intercept should throw IOException for 404 response`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody("Not Found"))

        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .build()

        try {
            okHttpClient.newCall(request).execute()
            Assert.fail("Exception was not thrown")
        } catch (e: IOException) {
            Assert.assertTrue(e.message!!.contains("Not Found: Not Found"))
        }
    }

    @Test
    fun `intercept should throw IOException for 500 response`() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody("Internal Server Error"))

        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .build()

        try {
            okHttpClient.newCall(request).execute()
            Assert.fail("Exception was not thrown")
        } catch (e: IOException) {
            Assert.assertTrue(e.message!!.contains("Internal Server Error: Internal Server Error"))
        }
    }
    // We can add more test for every case
}