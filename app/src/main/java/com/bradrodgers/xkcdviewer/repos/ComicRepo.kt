package com.bradrodgers.xkcdviewer.repos

import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.domain.ComicResponse

class ComicRepo(private val api: XkcdApi) {

    suspend fun getComic(comicNumber: Int): ComicResponse {

        val response = api.getComic(comicNumber)

        return if (response.isSuccessful) {
            ComicResponse.Data(comicInfo = response.body()!!)
        } else {
            ComicResponse.BlanketException(response.message())
        }
    }

    // Useful in the event you want to emit to local storage and observe the database rather than
    // observing the API call itself
//    fun getComic(comicNumber: Int = 0) = liveData<ComicInfo> {
//        try{
//            val comic = api.getComic(comicNumber)
//            emit(comic)
//        }catch (exception: IOException){
//            // Emit an error state once we get result polymorphic class going
//        }
//    }

    suspend fun getCurrentComic(): ComicResponse {
        val response = api.getCurrentComic()

        return if (response.isSuccessful) {
            ComicResponse.Data(comicInfo = response.body()!!)
        } else {
            ComicResponse.BlanketException(response.message())
        }
    }

}