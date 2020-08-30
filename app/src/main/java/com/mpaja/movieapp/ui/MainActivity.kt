package com.mpaja.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mpaja.movieapp.R
import com.mpaja.movieapp.di.Injectable
import com.mpaja.movieapp.ui.base.BaseActivity
import dagger.android.support.HasSupportFragmentInjector

class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}