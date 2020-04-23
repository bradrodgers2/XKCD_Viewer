package com.bradrodgers.xkcdviewer.domain

sealed class ComicResponse {
    data class Data(val comicInfo: ComicInfo): ComicResponse()
    object NoInternetException: ComicResponse()
    data class BlanketException(val throwable: Throwable): ComicResponse()
}