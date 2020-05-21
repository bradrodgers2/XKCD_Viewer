package com.bradrodgers.xkcdviewer.repos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.doa.ComicInfoDoa
import com.bradrodgers.xkcdviewer.doa.DatabaseTransferHelper.modelToDatabase
import com.bradrodgers.xkcdviewer.domain.ComicResponse
import com.bradrodgers.xkcdviewer.testData.TestData.Companion.comicDummyData
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class ComicRepoTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    var api: XkcdApi = mock {
        onBlocking { getComic(0) } doReturn Response.success(comicDummyData)
        val body = "".toResponseBody()
        onBlocking { getComic(-1) } doReturn Response.error(500, body)
        onBlocking { getCurrentComic() } doReturn Response.success(comicDummyData)
    }

    var dao: ComicInfoDoa = mock()
    var repo: ComicRepo = ComicRepo(api, dao)

    @Test
    fun get_comic_returns_comic() = runBlocking {
        val response = repo.getComic(0)

        verify(api).getComic(0)
        assertEquals(ComicResponse.Data(comicDummyData), response)
    }

    @Test
    fun get_comic_returns_error() = runBlocking {
        val response = repo.getComic(-1)
        verify(api).getComic(-1)
        assertEquals(ComicResponse.BlanketException("Response.error()"), response)
    }

    @Test
    fun get_current_comic_returns_comic() = runBlocking {
        val response = repo.getCurrentComic()

        verify(api, times(1)).getCurrentComic()
        assertEquals(ComicResponse.Data(comicDummyData), response)
    }

    @Test
    fun get_current_comic_returns_error() = runBlocking {
        val body = "".toResponseBody()
        whenever(api.getCurrentComic()).thenReturn(Response.error(500, body))

        val response = repo.getCurrentComic()
        verify(api).getCurrentComic()
        assertEquals(ComicResponse.BlanketException("Response.error()"), response)
    }

    @Test
    fun save_comic_pings_dao() = runBlocking {
        repo.saveComic(comicDummyData)
        verify(dao, times(1)).saveComic(refEq(modelToDatabase(comicDummyData)))
    }

//    @Test
//    fun get_saved_comics_returns_comics() = runBlocking {
//        whenever(dao.getSavedComics()).thenReturn(liveData { listOf(comicDummyData, comicDummyData) })
//        val response = repo.getSavedComics()
//        verify(dao, times(1)).getSavedComics()
//        assertEquals(listOf(comicDummyData, comicDummyData), response.value)
//    }

}