package com.mpaja.movieapp.di.modules

import com.mpaja.movieapp.ui.MainActivity
import com.mpaja.movieapp.di.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}