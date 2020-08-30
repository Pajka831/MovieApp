package com.mpaja.movieapp.di.modules

import com.mpaja.movieapp.di.FragmentScope
import com.mpaja.movieapp.ui.moviedetails.MovieDetailsFragment
import com.mpaja.movieapp.ui.movielist.MovieListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment(): MovieDetailsFragment
}