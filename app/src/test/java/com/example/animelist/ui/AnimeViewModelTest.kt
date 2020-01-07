package com.example.animelist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.animelist.QueryUtils
import com.example.animelist.entity.AnimeDetail
import com.example.animelist.entity.AnimeInfo
import com.example.animelist.mock
import com.example.animelist.network.AnimeNetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

import org.mockito.Mockito.`when`
import java.io.File

class AnimeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewmodel: AnimeViewModel

    private val observer: Observer<List<AnimeInfo>> = mock()

    private val observer2: Observer<AnimeDetail> = mock()

    private val queryUtil: QueryUtils = mock()

    private val animeNetworkService: AnimeNetworkService = mock()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewmodel = AnimeViewModel(queryUtil, animeNetworkService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun getAnimeListLiveData() {
        runBlocking {
            val animeinfo1 = AnimeInfo("1", "1", "TV", "testAnime", "", "")
            val animeinfo2 = AnimeInfo("1", "1", "TV", "testAnime", "", "")
            val string =
                File("src\\test\\java\\com\\example\\animelist\\ui\\testdata.txt").readText(Charsets.UTF_8)
            `when`(animeNetworkService.fetchAnimeList()).thenReturn(string)
            `when`(queryUtil.parseXmlToAnimeList(string)).thenReturn(listOf(animeinfo1, animeinfo2))
            viewmodel.animeListLiveData.observeForever(observer)
            delay(1000)
            val list = viewmodel.animeListLiveData
            assertEquals(list.value?.size, 2)
        }
    }

    @Test
    fun loadAnimeDetail() {
        val id = "testId"
        runBlocking {
            val animeDetail = AnimeDetail(
                "1",
                "1",
                "TV",
                "testAnime",
                "",
                "",
                "",
                "",
                null,
                null,
                "",
                "",
                null,
                "",
                "",
                ""
            )
            `when`(animeNetworkService.fetchAnimeDetail(id)).thenReturn(id)
            `when`(queryUtil.parseXmlToAnimeDetail(id)).thenReturn(animeDetail)
            viewmodel.loadAnimeDetail(id)
            viewmodel.animeDetailLiveData.observeForever(observer2)
            delay(1000)
            val detail = viewmodel.animeDetailLiveData.value
            assertEquals(detail?.name, animeDetail.name)
            assertEquals(detail?.id, animeDetail.id)
            assertEquals(detail?.gid, animeDetail.gid)
            assertEquals(detail?.type, animeDetail.type)
        }
    }

    @Test
    fun filterAnimeListByName() {
        runBlocking {
            val animeinfo1 = AnimeInfo("1", "1", "TV", "test", "", "")
            val animeinfo2 = AnimeInfo("2", "2", "TV", "filter", "", "")
            val string = "test"
            `when`(animeNetworkService.fetchAnimeList()).thenReturn(string)
            `when`(queryUtil.parseXmlToAnimeList(string)).thenReturn(listOf(animeinfo1, animeinfo2))
            viewmodel.animeListLiveData.observeForever(observer)
            delay(1000)
            viewmodel.filterAnimeListByName("filter")
            assertEquals(viewmodel.animeListLiveData.value?.size, 1)
        }
    }
}