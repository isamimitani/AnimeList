package com.example.animelist.entity

class AnimeDetail(
    id: String?, gid: String?,
    type: String?, name: String?,
    precision: String?, vintage: String?,
    var pictureUrl: String?, var japaneseTitle: String?,
    var genres: List<String>?, var themes: List<String>?,
    var plotSummary: String?, var numberOfEpisodes: String?,
    var officialWebSite: List<String>?, var rating: String?,
    var openingTheme: String?, var endingTheme: String?
) : AnimeInfo(id, gid, type, name, precision, vintage) {

    constructor() : this(
        null, null, null, null, null, null,
        null, null, mutableListOf(), mutableListOf(), null, null
        , mutableListOf(), null, null, null
    )
}