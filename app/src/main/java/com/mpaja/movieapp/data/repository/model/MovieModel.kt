package com.mpaja.movieapp.data.repository.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MovieModel(
    val id: Int,
    val title: String,
    val date: String,
    val rating: String,
    val overview: String,
    val backdropPath: String?,
    val posterPath: String?,
    var favorite: Boolean = false
) : Parcelable