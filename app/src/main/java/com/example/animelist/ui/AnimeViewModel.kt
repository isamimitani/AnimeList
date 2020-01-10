package com.example.animelist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.animelist.QueryUtils
import com.example.animelist.entity.AnimeDetail
import com.example.animelist.entity.AnimeInfo
import com.example.animelist.network.AnimeNetworkService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

enum class AnimeApiStatus { LOADING, ERROR, DONE }

@Singleton
class AnimeViewModel
@Inject constructor(
    private val queryUtils: QueryUtils,
    private val animeNetworkService: AnimeNetworkService
) : ViewModel() {

    companion object {
        @JvmField
        val TAG = AnimeViewModel::class.simpleName
    }

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _status = MutableLiveData<AnimeApiStatus>()
    val status: LiveData<AnimeApiStatus>
        get() = _status

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
    val navigateToSelectedAnimeDetail: LiveData<String>
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
                try {
                    _status.postValue(AnimeApiStatus.LOADING)
                    val result = animeNetworkService.fetchAnimeList()
                    animeList = queryUtils.parseXmlToAnimeList(result)
                    _status.postValue(AnimeApiStatus.DONE)
                    _animeListLiveData.postValue(animeList?.sortedBy { it.name })
                } catch (e:Exception){
                    _status.postValue(AnimeApiStatus.ERROR)
                    _animeListLiveData.postValue(ArrayList())
                }
            }
        }
    }

    // Do an asynchronous operation to fetch animeDetail
    fun loadAnimeDetail(animeId: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _status.postValue(AnimeApiStatus.LOADING)
                    val result = animeNetworkService.fetchAnimeDetail(animeId)
                    val animeDetail = queryUtils.parseXmlToAnimeDetail(result)
                    _status.postValue(AnimeApiStatus.DONE)
                    _animeDetailLiveData.postValue(animeDetail)
                } catch (e: Exception) {
                    _status.postValue(AnimeApiStatus.ERROR)
                }
            }
        }
    }

    fun filterAnimeListByName(text: String) {
        if (animeList != null && text != "") {
            _animeListLiveData.postValue(animeList?.filter {
                it.name?.contains(text, ignoreCase = true) ?: false
            }?.sortedBy { it.name })
        }
    }

}
