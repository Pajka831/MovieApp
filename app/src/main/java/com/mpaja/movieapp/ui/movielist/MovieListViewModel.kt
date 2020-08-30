package com.mpaja.movieapp.ui.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mpaja.movieapp.R
import com.mpaja.movieapp.data.repository.Repository
import com.mpaja.movieapp.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    private val _moviesUiState = MutableLiveData<MoviesUiState>()
    val moviesUiState: LiveData<MoviesUiState>
        get() = _moviesUiState

    private val _autoCorrectLiveData = MutableLiveData<List<String>>()
    val autoCorrectLiveData: LiveData<List<String>>
        get() = _autoCorrectLiveData

    private val _errorLiveData = MutableLiveData<Event<Int>>()
    val errorLiveData: LiveData<Event<Int>>
        get() = _errorLiveData

    fun getMovies() {
        compositeDisposable.add(
            repository.getMovies().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _moviesUiState.value = MoviesUiState.Loading }
                .subscribe({
                    _moviesUiState.value = MoviesUiState.DataLoaded(it)
                }, {
                    _moviesUiState.value = MoviesUiState.DataLoaded(null)
                })
        )
    }

    fun getAutoCorrect(query: String){
        compositeDisposable.add(
            repository.getSearchAutoCorrect(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _autoCorrectLiveData.value = it
                }, {
                    Log.d("Autocorrect", "Error while providing autocorrect")
                })
        )
    }

    fun searchMovies(query: String){
        compositeDisposable.add(
            repository.getSearchedMovies(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _moviesUiState.value = MoviesUiState.Loading }
                .subscribe({
                    _moviesUiState.value = MoviesUiState.DataLoaded(it)
                }, {
                    _moviesUiState.value = MoviesUiState.DataLoaded(null)
                })
        )
    }

    fun handleFavorite(id: Int, isFavorite: Boolean){
        compositeDisposable.add(
            repository.handleFavorite(id, isFavorite).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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