package com.bradrodgers.xkcdviewer.api

import com.bradrodgers.xkcdviewer.domain.ComicInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface XkcdApi {

    @GET("info.0.json")
    suspend fun getCurrentComic(): Response<ComicInfo>

    @GET("{comicNumber}/info.0.json")
    suspend fun getComic(@Path("comicNumber") comicNumber: Int): Response<ComicInfo>

}