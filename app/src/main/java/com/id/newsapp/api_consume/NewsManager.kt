package com.id.newsapp.api_consume

import retrofit2.http.GET
import retrofit2.Call
import com.id.newsapp.data.ResponseData
import retrofit2.http.Query

interface NewsManager {
    @GET("top-headlines")
    fun getLatestNews(@Query("sources")source: String, @Query("apiKey")apiKey: String): Call<ResponseData>

    @GET("top-headlines")
    fun getArticleListByCategoryId(
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String
    ): Call<ResponseData>

    @GET("top-headlines")
    fun searchAnyArticle(
        @Query("q") source: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") pageNumber: Int,
        @Query("apiKey")apiKey: String
    ): Call<ResponseData>
}