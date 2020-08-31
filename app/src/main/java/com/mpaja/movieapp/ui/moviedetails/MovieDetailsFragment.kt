package com.mpaja.movieapp.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mpaja.movieapp.R
import com.mpaja.movieapp.data.api.GlideApp
import com.mpaja.movieapp.data.repository.model.MovieModel
import com.mpaja.movieapp.databinding.FragmentMovieDetailsBinding
import com.mpaja.movieapp.di.ViewModelInjectionFactory
import com.mpaja.movieapp.ui.base.BaseFragment
import com.mpaja.movieapp.utils.EventObserver
import com.mpaja.movieapp.utils.viewModelProvider
import kotlinx.android.synthetic.main.fragment_movie_details.*
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelInjectionFactory: ViewModelInjectionFactory<MovieDetailsViewModel>

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = viewModelProvider(viewModelInjectionFactory)

        var movieModel: MovieModel? = null

        arguments?.let {
            movieModel = it.getParcelable<MovieModel>("movieModel")
            binding.model = movieModel
            context?.let { context ->
                GlideApp.with(context)
                    .load("https://image.tmdb.org/t/p/w500/${movieModel?.backdropPath}")
                    .into(image_background)
                GlideApp.with(context)
                    .load("https://image.tmdb.org/t/p/w500/${movieModel?.posterPath}")
                    .into(image_portrait)
            }
        }

        toggleButton.setOnCheckedChangeListener { button, isChecked ->
            movieModel?.let {
                if (isChecked) {
                    viewModel.handleFavorite(it.id, false)
                } else viewModel.handleFavorite(it.id, true)
            }
        }

        registerObservables()
    }

    private fun registerObservables() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
}