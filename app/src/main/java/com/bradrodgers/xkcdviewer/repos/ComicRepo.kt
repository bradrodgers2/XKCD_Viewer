package com.bradrodgers.xkcdviewer.repos

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.doa.ComicInfoDoa
import com.bradrodgers.xkcdviewer.doa.DatabaseTransferHelper
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse

class ComicRepo(private val api: XkcdApi, @VisibleForTesting val comicInfoDoa: ComicInfoDoa) {

    //TODO: Test coverage

    suspend fun getComic(comicNumber: Int): ComicResponse {
        val response = api.getComic(comicNumber)
        return if (response.isSuccessful) {
            ComicResponse.Data(comicInfo = response.body()!!)
        } else {
            ComicResponse.BlanketException(response.message())
        }
    }

    suspend fun saveComic(comicInfo: ComicInfo) {
        val databaseComicInfo = DatabaseTransferHelper.modelToDatabase(comicInfo)
        comicInfoDoa.saveComic(databaseComicInfo)
    }

    fun getSavedComics(): LiveData<List<ComicInfo>> = liveData {
        comicInfoDoa.getSavedComics().map { savedList ->
            return@map DatabaseTransferHelper.databaseListToModelList(savedList)
        }
    }

    suspend fun getCurrentComic(): ComicResponse {
        val response = api.getCurrentComic()
        return if (response.isSuccessful) {
            ComicResponse.Data(comicInfo = response.body()!!)
        } else {
            ComicResponse.BlanketException(response.message())
        }
    }

}