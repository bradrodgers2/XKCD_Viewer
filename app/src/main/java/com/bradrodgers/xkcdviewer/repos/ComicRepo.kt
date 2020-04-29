package com.bradrodgers.xkcdviewer.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.doa.ComicInfoDoa
import com.bradrodgers.xkcdviewer.doa.DatabaseTransferHelper
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse

class ComicRepo(private val api: XkcdApi, private val comicInfoDoa: ComicInfoDoa) {

    //TODO: Test coverage

    suspend fun getComic(comicNumber: Int): ComicResponse {
        val response = api.getComic(comicNumber)
        //TODO: Update to handle http codes

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

    //Likely not needed
//    fun getSavedComic(num: Int): LiveData<ComicInfo> = liveData {
//        comicInfoDoa.getSavedComic(num).map { databaseComicInfo ->
//            return@map DatabaseTransferHelper.databaseToModel(databaseComicInfo)
//        }
//    }

    suspend fun getCurrentComic(): ComicResponse {
        val response = api.getCurrentComic()
        //TODO: Update to handle http codes
        return if (response.isSuccessful) {
            ComicResponse.Data(comicInfo = response.body()!!)
        } else {
            ComicResponse.BlanketException(response.message())
        }
    }

}