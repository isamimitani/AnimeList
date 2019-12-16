package com.example.animelist.di

import com.example.animelist.ui.detail.AnimeDetailFragment
import com.example.animelist.ui.main.AnimeInfoFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent {
    // This tells Dagger that declared classes requests injection so the graph needs to
    // satisfy all the dependencies of the fields that declared classes is requesting.
    fun inject(animeDetailFragment: AnimeDetailFragment)
    fun inject(animeInfoFragment: AnimeInfoFragment)
}