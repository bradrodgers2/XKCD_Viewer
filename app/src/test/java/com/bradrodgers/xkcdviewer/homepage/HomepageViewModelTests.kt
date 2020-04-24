package com.bradrodgers.xkcdviewer.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse
import com.bradrodgers.xkcdviewer.repos.ComicRepo
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor

class HomepageViewModelTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    var homepageViewModel: HomepageViewModel = mock<HomepageViewModel>()
    var api: XkcdApi = mock<XkcdApi>()
    var repo: ComicRepo = mock<ComicRepo>()
    private val intObserver: Observer<Int> = mock()
    private val comicInfoObserver: Observer<ComicInfo> = mock()
    private val errorObserver: Observer<String> = mock()
    private val comicDummyData = ComicInfo(
        month = 4,
        num = 2297,
        link = "",
        year = 2020,
        news = "",
        safe_title = "Use or Discard By",
        transcript = "",
        alt = "One of the things of bear spray says that, and I'm not one to disobey safety instructions, but there are no bears around here. Guess it's time for a camping trip where we leave lots of food out!",
        img = "https://imgs.xkcd.com/comics/use_or_discard_by.png",
        title = "Use or Discard By",
        day = 22
    )

    @Before
    fun before(){
        homepageViewModel = HomepageViewModel(repo)
        homepageViewModel.comicNumberInput.observeForever(intObserver)
        homepageViewModel.comicInfo.observeForever(comicInfoObserver)
        homepageViewModel.errorStatement.observeForever(errorObserver)
    }

    @Test
    fun comic_number_livedata_gets_set() = runBlocking{
        whenever(repo.getComic(1)).thenReturn(mock())

        homepageViewModel.setComicNumber(0)

        val captor = ArgumentCaptor.forClass(Int::class.java)
        captor.run {
            verify(intObserver, times(1)).onChanged(capture())
            assertEquals(0, value)
        }
    }

    @Test
    fun comic_info_livedata_changes() = runBlocking {

        whenever(repo.getCurrentComic()).thenReturn(ComicResponse.Data(comicDummyData))

        homepageViewModel.setComicNumber(0)

        val captor = ArgumentCaptor.forClass(ComicInfo::class.java)
        captor.run {
            verify(comicInfoObserver, times(1)).onChanged(capture())
        }
    }

    @Test
    fun comic_info_gets_current_comic() = runBlocking {
        whenever(repo.getCurrentComic()).thenReturn(ComicResponse.Data(comicDummyData))

        homepageViewModel.setComicNumber(0)

        val captor = ArgumentCaptor.forClass(ComicInfo::class.java)
        captor.run{
            verify(repo, times(1)).getCurrentComic()
            verify(comicInfoObserver, times(1)).onChanged(capture())
            assertEquals(comicDummyData, value)
        }
    }

    @Test
    fun comic_info_gets_specific_comic() = runBlocking {
        whenever(repo.getComic(1)).thenReturn(ComicResponse.Data(comicDummyData))

        homepageViewModel.setComicNumber(1)

        val captor = ArgumentCaptor.forClass(ComicInfo::class.java)
        captor.run{
            verify(repo, times(1)).getComic(1)
            verify(comicInfoObserver, times(1)).onChanged(capture())
            assertEquals(comicDummyData, value)
        }
    }

    @Test
    fun comic_info_specific_returns_error() = runBlocking {
        whenever(repo.getComic(1)).thenReturn(ComicResponse.BlanketException("Here is a throwable"))

        homepageViewModel.setComicNumber(1)

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(repo, times(1)).getComic(1)
            verify(errorObserver, times(1)).onChanged(capture())
            assertEquals("An error has occurred. Try again later", value)
        }
    }

    @Test
    fun comic_info_current_returns_error() = runBlocking {
        whenever(repo.getCurrentComic()).thenReturn(ComicResponse.BlanketException("Here is a throwable"))

        homepageViewModel.setComicNumber(0)

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(repo, times(1)).getCurrentComic()
            verify(errorObserver, times(1)).onChanged(capture())
            assertEquals("An error has occurred. Try again later", value)
        }
    }
}