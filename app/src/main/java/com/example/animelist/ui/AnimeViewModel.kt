package com.example.animelist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animelist.entity.AnimeInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeViewModel @Inject constructor() : ViewModel() {

    companion object {
        val TAG = AnimeViewModel::class.simpleName
    }

    private val animeList: MutableLiveData<List<AnimeInfo>> by lazy {
        MutableLiveData<List<AnimeInfo>>().also {
            loadAnimeList()
        }
    }

    fun getAnimeList(): LiveData<List<AnimeInfo>> {
        return animeList
    }

    // Do an asynchronous operation to fetch animeList
    private fun loadAnimeList() {

    }
}
