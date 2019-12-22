package com.example.animelist.ui

import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.animelist.QueryUtils
import com.example.animelist.entity.AnimeDetail
import com.example.animelist.entity.AnimeInfo
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeViewModel @Inject constructor(private val queryUtils: QueryUtils) : ViewModel() {

    companion object {
        @JvmField
        val TAG = AnimeViewModel::class.simpleName
        const val ANIME_LIST_URL =
            "https://www.animenewsnetwork.com/encyclopedia/reports.xml?id=155&type=anime&nlist=all"
        const val ANIME_DETAIL_URL = "https://cdn.animenewsnetwork.com/encyclopedia/api.xml?anime="
    }

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _animeListLiveData: MutableLiveData<List<AnimeInfo>> by lazy {
        MutableLiveData<List<AnimeInfo>>().also {
            loadAnimeList()
        }
    }
    val animeListLiveData: LiveData<List<AnimeInfo>>
        get() = _animeListLiveData

    private var animeList: List<AnimeInfo>? = null

    private val _animeDetailLiveData: MutableLiveData<AnimeDetail> by lazy {
        MutableLiveData<AnimeDetail>()
    }
    val animeDetailLiveData: LiveData<AnimeDetail>
        get() = _animeDetailLiveData

    private val _navigateToSelectedAnimeDetail = MutableLiveData<String>()
    val navigateToSelectedAnimeDetail : LiveData<String>
        get() = _navigateToSelectedAnimeDetail

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

    fun displayAnimeDetail(animeId: String?) {
        _navigateToSelectedAnimeDetail.value = animeId
    }

    fun displayAnimeDetailComplete() {
        _navigateToSelectedAnimeDetail.value = null
    }

    // Do an asynchronous operation to fetch animeList
    private fun loadAnimeList() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                animeList = queryUtils.fetchAnimeListData(ANIME_LIST_URL)
                _animeListLiveData.postValue(animeList?.sortedBy { it.name })
            }
        }
    }

    // Do an asynchronous operation to fetch animeDetail
    fun loadAnimeDetail(animeId: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _animeDetailLiveData.postValue(queryUtils.fetchAnimeDetailData(ANIME_DETAIL_URL + animeId))
            }
        }
    }

    fun filterAnimeListByName(view: View) {
        if (animeList != null && view is EditText && !view.text.equals("")) {
            _animeListLiveData.postValue(animeList?.filter {
                it.name?.contains(view.text, ignoreCase = true) ?: false
            }?.sortedBy { it.name })
        }
    }
}
