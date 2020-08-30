package com.mpaja.movieapp.data.api

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.mpaja.movieapp.utils.enableTls12OnPreLollipop
import okhttp3.OkHttpClient
import java.io.InputStream


@GlideModule
class CustomOkHttpGlideModule : AppGlideModule() {


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()

        val okHttpClient = enableTls12OnPreLollipop(
            builder
        ).build()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }
}