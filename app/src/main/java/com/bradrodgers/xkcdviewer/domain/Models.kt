package com.bradrodgers.xkcdviewer.domain

data class ComicInfo(
    val num: Int = 0,
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val link: String = "",
    val news: String = "",
    val safe_title: String = "",
    val transcript: String = "",
    val alt: String = "",
    val img: String = "",
    val title: String = ""
)