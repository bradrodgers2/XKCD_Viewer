package com.bradrodgers.xkcdviewer.doa

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_comic_info")
class DatabaseComicInfo(
    @PrimaryKey
    val num: Int,
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