package com.bradrodgers.xkcdviewer.homepage

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse
import com.bradrodgers.xkcdviewer.repos.ComicRepo
import kotlinx.coroutines.launch

class HomepageViewModel(private val repo: ComicRepo) : ViewModel() {

    // Investigate this https://developer.android.com/topic/libraries/architecture/coroutines

    //TODO: Test coverage!

    @VisibleForTesting
    val comicNumberInput: MutableLiveData<Int> = MutableLiveData<Int>()
    val comicInfo: LiveData<ComicInfo> =
        Transformations.switchMap(comicNumberInput) { comicNumber ->
            liveData {
                when (comicNumber) {
                    0 -> {
                        val response = repo.getCurrentComic()
                        if (response is ComicResponse.Data) {
                            emit(response.comicInfo)
                        } else {
                            setErrorStatement("An error has occurred. Try again later")
                        }
                    }
                    else -> {
                        val response = repo.getComic(comicNumber)
                        if (response is ComicResponse.Data) {
                            emit(response.comicInfo)
                        } else {
                            setErrorStatement("An error has occurred. Try again later")
                        }
                    }
                }
            }
        }

    fun setComicNumber(number: Int) {
        comicNumberInput.value = number
    }

    fun saveComic(comicInfo: ComicInfo) {
        viewModelScope.launch {
            repo.saveComic(comicInfo)
        }
    }

    val savedComics: LiveData<List<ComicInfo>> = liveData {
        val comics = repo.getSavedComics()
        comics.map {
            return@map it
        }
    }

    val errorStatement: LiveData<String>
        get() {
            return _errorStatement
        }
    private val _errorStatement: MutableLiveData<String> = MutableLiveData()

    private fun setErrorStatement(statement: String) {
        _errorStatement.value = statement
    }
}
