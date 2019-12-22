package com.example.animelist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.animelist.QueryUtils
import com.example.animelist.entity.AnimeInfo
import com.example.animelist.mock
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mock
import java.util.*

import org.mockito.Mockito.`when`


class AnimeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewmodel : AnimeViewModel

    private val observer : Observer<List<AnimeInfo>> = mock()

    private val queryUtil : QueryUtils = mock()

    @Before
    fun setUp() {
        viewmodel = AnimeViewModel(queryUtil)
        viewmodel.animeListLiveData.observeForever(observer)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getAnimeListLiveData() {
        val animeinfo1 = AnimeInfo("1","1","TV","testAnime","","")
        val animeinfo2 = AnimeInfo("1","1","TV","testAnime","","")
        `when`(queryUtil.fetchAnimeListData(AnimeViewModel.ANIME_LIST_URL)).thenReturn(listOf(animeinfo1, animeinfo2))
        val list = viewmodel.animeListLiveData
        assertEquals(list.value?.size, 2)
    }

    @Test
    fun loadAnimeDetail() {
    }

    @Test
    fun filterAnimeListByName() {
    }
}