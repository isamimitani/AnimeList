package com.example.animelist

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class QueryUtilsTest {

    private lateinit var queryUtils: QueryUtils

    val animeListString =
        File("src\\test\\java\\com\\example\\animelist\\testdata\\animeList.txt").readText(Charsets.UTF_8)

    val animeDetailString =
        File("src\\test\\java\\com\\example\\animelist\\testdata\\animeDetail.txt").readText(Charsets.UTF_8)

    @Before
    fun setUp() {
        queryUtils = QueryUtils()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun parseXmlToAnimeList() {
        val animeList = queryUtils.parseXmlToAnimeList(animeListString)
        assertEquals(animeList.size, 2)
        assertEquals(animeList.get(0).name, "Mobile Suit Gundam G40")
        assertEquals(animeList.get(0).id, "22961")
        assertEquals(animeList.get(0).gid, "1454700421")
        assertEquals(animeList.get(0).precision, "ONA")
        assertEquals(animeList.get(0).type, "ONA")
        assertEquals(animeList.get(0).vintage, "2020-01-01")
        assertEquals(animeList.get(1).name, "Mobile Suit Gundam Hathaway")
    }

    @Test
    fun parseXmlToAnimeDetail() {
        val animeDetail = queryUtils.parseXmlToAnimeDetail(animeDetailString)
        assertEquals(animeDetail?.id, "14781")
        assertEquals(animeDetail?.gid, "2713754865")
        assertEquals(animeDetail?.name, "Chihayafuru 2")
        assertEquals(animeDetail?.japaneseTitle, "ちはやふる2")
        assertEquals(
            animeDetail?.pictureUrl,
            "https://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A14781-3662993362.1354589590.jpg"
        )
        assertEquals(animeDetail?.officialWebSite?.size, 1)
        assertEquals(animeDetail?.endingTheme, "\"Akanezora\" by Asami Seto")
        assertEquals(animeDetail?.numberOfEpisodes, null)
        assertEquals(animeDetail?.openingTheme, "\"STAR\" by 99RadioService")
        assertEquals(animeDetail?.genres?.size, 3)
        assertEquals(animeDetail?.themes?.size, 3)
        assertTrue(
            animeDetail?.plotSummary?.contains("After an exciting year for the Mizusawa High School karuta club")
                ?: false
        )
    }
}