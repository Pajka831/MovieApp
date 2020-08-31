package com.mpaja.movieapp.ui.movielist.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mpaja.movieapp.R
import com.mpaja.movieapp.data.repository.model.MovieModel
import com.mpaja.movieapp.databinding.ItemMovieBinding
import kotlinx.android.synthetic.main.item_movie.view.*
import javax.inject.Inject

class MovieRVAdapter @Inject constructor() :
    ListAdapter<MovieModel, RecyclerView.ViewHolder>(MovieDiffUtils()) {

    var onItemClickedListener: OnItemClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMovieBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                val movie = getItem(position)
                holder.binding.model = movie

                holder.itemView.setOnClickListener {
                    onItemClickedListener?.onClick(movie)
                }

                holder.itemView.toggleButton.setOnCheckedChangeListener { button, isChecked ->
                    if (button.isPressed) {
                        if (isChecked) {
                            movie.favorite = true
                            onItemClickedListener?.onFavoriteClick(movie.id, isFavorite = false)
                        } else {
                            movie.favorite = false
                            onItemClickedListener?.onFavoriteClick(movie.id, isFavorite = true)
                        }
                    }
                }
            }
        }

    }

    fun addMovies(movies: List<MovieModel>) {
        submitList(movies)
    }

    class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickedListener {
        fun onClick(movieModel: MovieModel)
        fun onFavoriteClick(id: Int, isFavorite: Boolean)
    }
}

class MovieDiffUtils : DiffUtil.ItemCallback<MovieModel>() {
    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel) =
        oldItem.id == newItem.id

    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel) = oldItem == newItem
}