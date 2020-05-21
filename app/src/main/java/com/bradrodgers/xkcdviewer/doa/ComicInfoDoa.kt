package com.bradrodgers.xkcdviewer.doa

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ComicInfoDoa {
    @Query("SELECT * FROM saved_comic_info WHERE num != 0")
    fun getSavedComics(): LiveData<List<DatabaseComicInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveComic(comicInfo: DatabaseComicInfo)
}