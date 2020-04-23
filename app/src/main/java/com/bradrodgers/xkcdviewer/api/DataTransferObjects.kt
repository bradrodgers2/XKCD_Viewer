package com.bradrodgers.xkcdviewer.api

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkComicInfo(
    val id: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val link: String,
    val news: String,
    val safe_title: String,
    val transcript: String,
    val alt: String,
    val img: String,
    val title: String
)