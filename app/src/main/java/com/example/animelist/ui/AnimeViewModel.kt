package com.example.animelist.ui

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.animelist.QueryUtils
import com.example.animelist.entity.AnimeDetail
import com.example.animelist.entity.AnimeInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeViewModel @Inject constructor() : ViewModel() {

    companion object {
        @JvmField
        val TAG = AnimeViewModel::class.simpleName
        const val ANIME_LIST_URL =
            "https://www.animenewsnetwork.com/encyclopedia/reports.xml?id=155&type=anime&nlist=all"
        const val ANIME_DETAIL_URL = "https://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime="
    }

    private val animeListLiveData: MutableLiveData<List<AnimeInfo>> by lazy {
        MutableLiveData<List<AnimeInfo>>().also {
            loadAnimeList()
        }
    }

    private var animeList: List<AnimeInfo>? = null

    private val animeDetailLiveData: MutableLiveData<AnimeDetail> by lazy {
        MutableLiveData<AnimeDetail>()
    }

    // Transform AnimeDetail to LiveData to use them in data binding
    val id: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.id }
    val name: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.name }
    val japaneseTitle: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.japaneseTitle }
    val plotSummary: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.plotSummary }
    val openingTheme: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.openingTheme }
    val endingTheme: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.endingTheme }
    val vintage: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.vintage }
    val numberOfEpisodes: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail -> animeDetail.numberOfEpisodes }
    val officialWebsite: LiveData<String> =
        Transformations.map(animeDetailLiveData) { animeDetail ->
            animeDetail.officialWebSite?.joinToString("\r\n")
        }

    fun getAnimeListLiveData(): LiveData<List<AnimeInfo>> {
        return animeListLiveData
    }

    fun getAnimeDetailLiveData(): LiveData<AnimeDetail> {
        return animeDetailLiveData
    }

    // Do an asynchronous operation to fetch animeList
    private fun loadAnimeList() {
        AsyncTask.execute {
            animeList = QueryUtils.fetchAnimeListData(ANIME_LIST_URL)
            animeListLiveData.postValue(animeList?.sortedBy { it.name })
        }
    }

    // Do an asynchronous operation to fetch animeDetail
    fun loadAnimeDetail(animeId: String) {
        AsyncTask.execute {
            animeDetailLiveData.postValue(QueryUtils.fetchAnimeDetailData(ANIME_DETAIL_URL + animeId))
        }
    }

    fun filterAnimeListByName(searchString: String) {
        animeListLiveData.postValue(animeList?.filter { it.name?.contains(searchString) ?: false })
    }
}
