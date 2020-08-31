package com.mpaja.movieapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mpaja.movieapp.R
import com.mpaja.movieapp.data.repository.Repository
import com.mpaja.movieapp.utils.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    private val _errorLiveData = MutableLiveData<Event<Int>>()
    val errorLiveData: LiveData<Event<Int>>
        get() = _errorLiveData

    fun handleFavorite(id: Int, isFavorite: Boolean) {
        compositeDisposable.add(
            repository.handleFavorite(id, isFavorite).subscribeOn(Schedulers.io())
                .subscribe({

                }, {
                    _errorLiveData.value = Event(R.string.error_favorite)
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}