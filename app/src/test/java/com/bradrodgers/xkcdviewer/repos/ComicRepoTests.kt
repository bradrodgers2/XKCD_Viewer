package com.bradrodgers.xkcdviewer.repos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.doa.ComicInfoDoa
import com.bradrodgers.xkcdviewer.doa.DatabaseTransferHelper.modelToDatabase
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse
import com.bradrodgers.xkcdviewer.testData.TestData.Companion.comicDummyData
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
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
    private val savedComicObserver: Observer<List<ComicInfo>> = mock()

    @Before
    fun before() {
        // This exists to activate the saved comics list live data to verify dao interaction
        repo.savedComics.observeForever(savedComicObserver)
    }

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
    fun save_comic_updates_saved_comics() = runBlocking {
        repo.saveComic(comicDummyData)
        verify(dao, times(1)).getSavedComics()
        verify(dao, times(1)).saveComic(refEq(modelToDatabase(comicDummyData)))
    }
}