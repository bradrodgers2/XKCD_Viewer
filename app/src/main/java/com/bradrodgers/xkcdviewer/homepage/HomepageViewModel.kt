package com.bradrodgers.xkcdviewer.homepage

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.bradrodgers.xkcdviewer.domain.ComicInfo
import com.bradrodgers.xkcdviewer.domain.ComicResponse
import com.bradrodgers.xkcdviewer.repos.ComicRepo
import timber.log.Timber

class HomepageViewModel(private val repo: ComicRepo) : ViewModel() {

    // Investigate this https://developer.android.com/topic/libraries/architecture/coroutines

    @VisibleForTesting
    val comicNumberInput: MutableLiveData<Int> = MutableLiveData<Int>()
    val comicInfo: LiveData<ComicInfo> =
            Transformations.switchMap(comicNumberInput) {comicNumber ->
            liveData {
                if(comicNumber == 0) {
                    when (val response = repo.getCurrentComic()) {
                        ComicInfo() -> {
                            //noop
                        }
                        else -> {
                            emit(response)
                        }
                    }
                }else{
                    when (val response = repo.getComic(comicNumber)) {
                        ComicInfo() -> {
                            //noop
                        }
                        else -> {
                            emit(response)
                        }
                    }
                }
            }
        }

    fun setComicNumber(number: Int){
        Timber.d("setting number to: $number")
        comicNumberInput.value = number
    }

    fun handleError(response: ComicResponse): LiveData<Pair<String, String>>{
        return liveData {
            when(response){
                is ComicResponse.NoInternetException -> {
                    emit(Pair("No Internet",
                        "We could not get a valid internet connection. Try again later.")
                    )
                }
                is ComicResponse.BlanketException -> {
                    Timber.e(response.throwable, "Blanket exception found: ")
                    emit(Pair("Unknown Error", "An error occurred. Try again later."))
                }
            }
        }

    }
}
