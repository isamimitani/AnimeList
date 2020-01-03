/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.animelist

import android.util.Log
import android.util.Xml
import com.example.animelist.entity.AnimeDetail
import com.example.animelist.entity.AnimeInfo
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
@Singleton
class QueryUtils @Inject constructor() {

    companion object {
        @JvmField
        val LOG_TAG = QueryUtils::class.java.simpleName

        // We don't use namespaces
        private val ns: String? = null
    }

//    /**
//     * Query the Anime News Network dataset and return a list of [AnimeInfo] objects.
//     */
//    fun fetchAnimeListData(requestUrl: String): List<AnimeInfo>? { // Create URL object
//        val url = createUrl(requestUrl)
//        // Perform HTTP request to the URL and receive a JSON response back
//        var xmlResponse: String? = null
//        try {
//            xmlResponse = makeHttpRequest(url)
//        } catch (e: IOException) {
//            Log.e(LOG_TAG, "Problem making the HTTP request.", e)
//        }
//        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
//        // Return the list of {@link Earthquake}s
//        //return extractFeatureFromXML(xmlResponse)
//        return parseXmlToAnimeList(xmlResponse)
//    }

//    /**
//     * Query the Anime News Network dataset and return a [AnimeDetail] objects.
//     */
//    fun fetchAnimeDetailData(requestUrl: String): AnimeDetail? { // Create URL object
//        val url = createUrl(requestUrl)
//        // Perform HTTP request to the URL and receive a JSON response back
//        var xmlResponse: String? = null
//        try {
//            xmlResponse = makeHttpRequest(url)
//        } catch (e: IOException) {
//            Log.e(LOG_TAG, "Problem making the HTTP request.", e)
//        }
//        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
//        // Return the list of {@link Earthquake}s
//        //return extractFeatureFromXML(xmlResponse)
//        return parseXmlToAnimeDetail(xmlResponse)
//    }
//
//    /**
//     * Returns new URL object from the given string URL.
//     */
//    private fun createUrl(stringUrl: String): URL? {
//        var url: URL? = null
//        try {
//            url = URL(stringUrl)
//        } catch (e: MalformedURLException) {
//            Log.e(LOG_TAG, "Problem building the URL ", e)
//        }
//        return url
//    }
//
//    /**
//     * Make an HTTP request to the given URL and return a String as the response.
//     */
//    @Throws(IOException::class)
//    private fun makeHttpRequest(url: URL?): String {
//        var xmlResponse = ""
//        // If the URL is null, then return early.
//        if (url == null) {
//            return xmlResponse
//        }
//        var urlConnection: HttpURLConnection? = null
//        var inputStream: InputStream? = null
//        try {
//            urlConnection = url.openConnection() as HttpURLConnection
//            urlConnection.readTimeout = 10000
//            urlConnection.connectTimeout = 15000
//            urlConnection.requestMethod = "GET"
//            urlConnection.connect()
//            // If the request was successful (response code 200),
//            // then read the input stream and parse the response.
//            if (urlConnection.responseCode == 200) {
//                inputStream = urlConnection.inputStream
//                xmlResponse = readFromStream(inputStream)
//                println(xmlResponse)
//            } else {
//                Log.e(
//                    LOG_TAG,
//                    "Error response code: " + urlConnection.responseCode
//                )
//            }
//        } catch (e: IOException) {
//            Log.e(
//                LOG_TAG,
//                "Problem retrieving the earthquake JSON results.",
//                e
//            )
//        } finally {
//            urlConnection?.disconnect()
//            inputStream?.close()
//        }
//        return xmlResponse
//    }
//
//    /**
//     * Convert the [InputStream] into a String which contains the
//     * whole JSON response from the server.
//     */
//    @Throws(IOException::class)
//    private fun readFromStream(inputStream: InputStream?): String {
//        val output = StringBuilder()
//        if (inputStream != null) {
//            val inputStreamReader =
//                InputStreamReader(inputStream, Charset.forName("UTF-8"))
//            val reader = BufferedReader(inputStreamReader)
//            var line = reader.readLine()
//            while (line != null) {
//                output.append(line)
//                line = reader.readLine()
//            }
//        }
//        return output.toString()
//    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parseXmlToAnimeList(xmlResponse: String?): List<AnimeInfo> {
        val inputStream = ByteArrayInputStream(xmlResponse?.toByteArray(Charsets.UTF_8))
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readReport(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parseXmlToAnimeDetail(xmlResponse: String?): AnimeDetail? {
        val inputStream = ByteArrayInputStream(xmlResponse?.toByteArray(Charsets.UTF_8))
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readAnn(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readReport(parser: XmlPullParser): List<AnimeInfo> {
        val items = mutableListOf<AnimeInfo>()
        parser.require(XmlPullParser.START_TAG, ns, "report")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "item") {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readAnn(parser: XmlPullParser): AnimeDetail? {
        var animeDetail = AnimeDetail()

        parser.require(XmlPullParser.START_TAG, ns, "ann")
        while (parser.next() != XmlPullParser.END_TAG && parser.name != "ann") {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "anime") {
                readAnime(parser, animeDetail)
            } else if (parser.name == "info") {
                readInfo(parser, animeDetail)
            } else if (parser.name == "ratings") {
                readRatings(parser, animeDetail)
            } else {
                skip(parser)
            }
        }
        return animeDetail
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readItem(parser: XmlPullParser): AnimeInfo {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var id: String? = null
        var gid: String? = null
        var type: String? = null
        var name: String? = null
        var precision: String? = null
        var vintage: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "id" -> id = readId(parser)
                "gid" -> gid = readGid(parser)
                "type" -> type = readType(parser)
                "name" -> name = readName(parser)
                "precision" -> precision = readPrecision(parser)
                "vintage" -> vintage = readVintage(parser)
                else -> skip(parser)
            }
        }
        return AnimeInfo(id, gid, type, name, precision, vintage)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readAnime(parser: XmlPullParser, animeDetail: AnimeDetail): AnimeDetail {
        parser.require(XmlPullParser.START_TAG, ns, "anime")
        animeDetail.id = parser.getAttributeValue(null, "id")
        animeDetail.gid = parser.getAttributeValue(null, "gid")
        animeDetail.type = parser.getAttributeValue(null, "type")
        animeDetail.name = parser.getAttributeValue(null, "name")
        animeDetail.precision = parser.getAttributeValue(null, "precision")
        return animeDetail
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readInfo(parser: XmlPullParser, animeDetail: AnimeDetail): AnimeDetail {
        parser.require(XmlPullParser.START_TAG, ns, "info")
        val attrType = parser.getAttributeValue(null, "type")
        when (attrType) {
            "Picture" -> {
                animeDetail.pictureUrl = parser.getAttributeValue(null, "src")
                skip(parser)
            }
            "Alternative title" -> if (parser.getAttributeValue(null, "lang") == "JA") {
                animeDetail.japaneseTitle = readText(parser)
            }
            "Genres" -> (animeDetail.genres as MutableList).add(readText(parser))
            "Themes" -> (animeDetail.themes as MutableList).add(readText(parser))
            "Plot Summary" -> animeDetail.plotSummary = readText(parser)
            "Number of episodes" -> animeDetail.numberOfEpisodes = readText(parser)
            "Vintage" -> animeDetail.vintage = readText(parser)
            "Opening Theme" -> animeDetail.openingTheme = readText(parser)
            "Ending Theme" -> animeDetail.endingTheme = readText(parser)
            "Official website" -> (animeDetail.officialWebSite as MutableList).add(
                parser.getAttributeValue(
                    null,
                    "href"
                )
            )
            else -> skip(parser)
        }
        return animeDetail
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readRatings(parser: XmlPullParser, animeDetail: AnimeDetail): AnimeDetail {
        parser.require(XmlPullParser.START_TAG, ns, "ratings")
        animeDetail.rating = parser.getAttributeValue(null, "weighted_score")
        return animeDetail
    }

    // Processes id tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readId(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "id")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "id")
        return title
    }

    // Processes gid tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readGid(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "gid")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "gid")
        return title
    }

    // Processes type tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readType(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "type")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "type")
        return summary
    }

    // Processes name tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "name")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "name")
        return summary
    }

    // Processes precision tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readPrecision(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "precision")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "precision")
        return summary
    }

    // Processes vintage tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readVintage(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "vintage")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "vintage")
        return summary
    }

    // For the tags title and summary, extracts their text values.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}