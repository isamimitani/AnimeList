package com.example.animelist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario.launch
import com.example.animelist.QueryUtils
import com.example.animelist.entity.AnimeInfo
import com.example.animelist.mock
import com.example.animelist.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
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

    private val network : Network = mock()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewmodel = AnimeViewModel(queryUtil)
//        viewmodel.animeListLiveData.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun getAnimeListLiveData() {
        val animeinfo1 = AnimeInfo("1","1","TV","testAnime","","")
        val animeinfo2 = AnimeInfo("1","1","TV","testAnime","","")
        val result =  "mock"
        `when`(queryUtil.parseXmlToAnimeList(result)).thenReturn(listOf(animeinfo1, animeinfo2))
        `when`(queryUtil.parseXmlToAnimeList(result)).thenReturn(listOf(animeinfo1, animeinfo2))
        viewmodel.animeListLiveData.observeForever(observer)
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