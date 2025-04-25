package com.example.findest.data.remote.api

import com.example.findest.data.remote.response.DetailResponse
import com.example.findest.data.remote.response.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/products")
    suspend fun getAllProducts(): ProductResponse

    @GET("api/products/{id}")
    suspend fun getDetailProduct(
        @Path("id") id: Int
    ): DetailResponse
}