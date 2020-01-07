package com.example.animelist.di

import com.example.animelist.network.AnimeNetworkService
import com.example.animelist.network.AnimeNetworkServiceImpl
import dagger.Module
import dagger.Provides

// @Module informs Dagger that this class is a Dagger Module
@Module
class NetworkModule {

    // @Provides tell Dagger how to create instances of the type that this function
    // returns (i.e. AnimeNetworkService).
    // Function parameters are the dependencies of this type.
    @Provides
    fun provideAnimeNetworkService(): AnimeNetworkService {
        // Whenever Dagger needs to provide an instance of type AnimeNetworkService,
        // this code (the one inside the @Provides method) is run.
        return AnimeNetworkServiceImpl()
    }
}