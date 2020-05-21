package com.bradrodgers.xkcdviewer.doa

import com.bradrodgers.xkcdviewer.domain.ComicInfo

object DatabaseTransferHelper {
    fun databaseListToModelList(databaseComicInfo: List<DatabaseComicInfo>): List<ComicInfo> {
        return databaseComicInfo.map {
            ComicInfo(
                num = it.num,
                day = it.day,
                month = it.month,
                year = it.year,
                link = it.link,
                news = it.news,
                safe_title = it.safe_title,
                transcript = it.transcript,
                alt = it.alt,
                img = it.img,
                title = it.title
            )
        }
    }

    fun modelToDatabase(comicInfo: ComicInfo): DatabaseComicInfo {
        return DatabaseComicInfo(
            num = comicInfo.num,
            day = comicInfo.day,
            month = comicInfo.month,
            year = comicInfo.year,
            link = comicInfo.link,
            news = comicInfo.news,
            safe_title = comicInfo.safe_title,
            transcript = comicInfo.transcript,
            alt = comicInfo.alt,
            img = comicInfo.img,
            title = comicInfo.title
        )
    }
}