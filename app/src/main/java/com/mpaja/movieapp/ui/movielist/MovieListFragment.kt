package com.mpaja.movieapp.ui.movielist

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mpaja.movieapp.R
import com.mpaja.movieapp.data.repository.model.MovieModel
import com.mpaja.movieapp.databinding.FragmentMovieListBinding
import com.mpaja.movieapp.di.ViewModelInjectionFactory
import com.mpaja.movieapp.ui.base.BaseFragment
import com.mpaja.movieapp.ui.movielist.adapter.AutoCompleteAdapter
import com.mpaja.movieapp.ui.movielist.adapter.MovieRVAdapter
import com.mpaja.movieapp.utils.EventObserver
import com.mpaja.movieapp.utils.showErrorDialog
import com.mpaja.movieapp.utils.viewModelProvider
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject


class MovieListFragment : BaseFragment(), MovieRVAdapter.OnItemClickedListener,
    DialogInterface.OnClickListener {

    @Inject
    lateinit var viewModelInjectionFactory: ViewModelInjectionFactory<MovieListViewModel>

    lateinit var viewModel: MovieListViewModel
    lateinit var binding: FragmentMovieListBinding

    lateinit var movieListAdapter: MovieRVAdapter
    lateinit var autoCorrectAdapter: AutoCompleteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelInjectionFactory)


        prepareRecyclerView()
        registerObservables()
        prepareAutoCorrect()


        viewModel.getMovies()
    }

    private fun prepareAutoCorrect() {
        autoCorrectAdapter = AutoCompleteAdapter(requireContext())

        image_search.setOnClickListener {
            if (autoCompleteTextView.length() > 0) {
                autoCompleteTextView.setText("")
                viewModel.getMovies()
            }
        }

        autoCompleteTextView.setAdapter(autoCorrectAdapter)
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                if (count > 0) {
                    image_search.setImageResource(R.drawable.ic_close)
                    image_search.isClickable = true
                } else {
                    image_search.setImageResource(R.drawable.ic_search)
                    image_search.isClickable = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.getAutoCorrect(p0.toString())
            }
        })

        autoCompleteTextView.setOnEditorActionListener { textView, i, keyEvent ->
            if (EditorInfo.IME_ACTION_SEARCH == i) {
                viewModel.searchMovies(autoCompleteTextView.text.toString())
                autoCompleteTextView.dismissDropDown()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            viewModel.searchMovies(autoCompleteTextView.text.toString())
            autoCompleteTextView.dismissDropDown()
        }
    }

    private fun registerObservables() {
        viewModel.moviesUiState.observe(viewLifecycleOwner, Observer { uiState ->
            when (uiState) {
                is MoviesUiState.DataLoaded -> {
                    progressBar.visibility = View.GONE
                    if (uiState.moviesModel == null) {
                        showErrorDialog(
                            R.string.error_movie_list, this, R.string.error_movie_button,
                            false
                        )
                    } else {
                        movieListAdapter.addMovies(uiState.moviesModel)
                    }
                }
                MoviesUiState.Loading -> progressBar.visibility = View.VISIBLE
            }
        })

        viewModel.autoCorrectLiveData.observe(viewLifecycleOwner, Observer {
            autoCorrectAdapter.updateItems(it)
        })

        viewModel.errorLiveData.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun prepareRecyclerView() {
        movieListAdapter = MovieRVAdapter()
        movieListAdapter.onItemClickedListener = this
        recycler_movies.adapter = movieListAdapter
        recycler_movies.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onClick(movieModel: MovieModel) {
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
                movieModel
            )
        )
    }

    override fun onFavoriteClick(id: Int, isFavorite: Boolean) {
        viewModel.handleFavorite(id, isFavorite)
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        viewModel.getMovies()
    }
}

sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class DataLoaded(val moviesModel: List<MovieModel>?) : MoviesUiState()
}