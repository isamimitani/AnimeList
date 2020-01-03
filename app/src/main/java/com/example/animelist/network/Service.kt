package com.example.animelist.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.animenewsnetwork.com/"

interface AnimeService {
    @GET("encyclopedia/reports.xml?id=155&type=anime&nlist=all")
    suspend fun getAnimeList(): String

    @GET("encyclopedia/api.xml")
    suspend fun getAnimeDetail(@Query("anime") animeId: String): String
}

object Network {
    // Configure retrofit to parse XML and use coroutines
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val animeService = retrofit.create(AnimeService::class.java)
}

