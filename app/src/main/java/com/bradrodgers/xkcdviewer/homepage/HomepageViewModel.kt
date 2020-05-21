package com.bradrodgers.xkcdviewer.homepage

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse
import com.bradrodgers.xkcdviewer.repos.ComicRepo
import kotlinx.coroutines.launch
import timber.log.Timber

class HomepageViewModel(private val repo: ComicRepo) : ViewModel() {

    // Investigate this https://developer.android.com/topic/libraries/architecture/coroutines

    @VisibleForTesting
    val comicNumberInput: MutableLiveData<Int> = MutableLiveData<Int>()
    val comicInfo: LiveData<ComicInfo> =
        Transformations.switchMap(comicNumberInput) { comicNumber ->
            liveData {
                when (comicNumber) {
                    0 -> {
                        when (val response = repo.getCurrentComic()) {
                            is ComicResponse.Data -> {
                                emit(response.comicInfo)
                            }
                            is ComicResponse.BlanketException -> {
                                Timber.e(response.throwable)
                                setErrorStatement("An error has occurred. Try again later")
                            }
                        }
                    }
                    else -> {
                        when (val response = repo.getComic(comicNumber)) {
                            is ComicResponse.Data -> {
                                emit(response.comicInfo)
                            }
                            is ComicResponse.BlanketException -> {
                                Timber.e(response.throwable)
                                setErrorStatement("An error has occurred. Try again later")
                            }
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
        repo.getSavedComics().map {
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
