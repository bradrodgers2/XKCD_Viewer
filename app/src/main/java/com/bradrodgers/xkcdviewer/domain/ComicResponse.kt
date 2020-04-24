package com.bradrodgers.xkcdviewer.domain

sealed class ComicResponse {
    data class Data(val comicInfo: ComicInfo): ComicResponse()
    data class BlanketException(val throwable: String): ComicResponse()
}