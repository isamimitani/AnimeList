package com.example.animelist.network

interface AnimeNetworkService {
    suspend fun fetchAnimeList(): String
    suspend fun fetchAnimeDetail(animeId: String): String
}

class AnimeNetworkServiceImpl : AnimeNetworkService {
    override suspend fun fetchAnimeList(): String {
        return Network.animeService.getAnimeList()
    }

    override suspend fun fetchAnimeDetail(animeId: String): String {
        return Network.animeService.getAnimeDetail(animeId)
    }
}