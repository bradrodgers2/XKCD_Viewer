package com.bradrodgers.xkcdviewer.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bradrodgers.xkcdviewer.api.XkcdApi
import com.bradrodgers.xkcdviewer.domain.ComicInfo
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
    }

    @Test
    fun comic_number_livedata_gets_set() = runBlocking{
        whenever(api.getComic(1)).thenReturn(mock())

        homepageViewModel.setComicNumber(0)

        val captor = ArgumentCaptor.forClass(Int::class.java)
        captor.run {
            verify(intObserver, times(1)).onChanged(capture())
            assertEquals(0, value)
        }
    }

    @Test
    fun comic_info_livedata_changes() = runBlocking {

        whenever(api.getCurrentComic()).thenReturn(comicDummyData)

        homepageViewModel.setComicNumber(0)

        val captor = ArgumentCaptor.forClass(ComicInfo::class.java)
        captor.run {
            verify(comicInfoObserver, times(1)).onChanged(capture())
        }
    }
}