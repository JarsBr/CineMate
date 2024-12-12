package com.cinemate.app.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinemate.app.R
import com.cinemateapp.utils.Constants.Plataforma
import androidx.recyclerview.widget.ListAdapter
import com.cinemate.app.data.models.Movie
import com.cinemateapp.utils.Constants

class MoviesAdapter {

    class GenerosAdapter(
        private val generosList: List<String>,
        private val selectedGeneros: List<String>,
        private val onGeneroChecked: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<GenerosAdapter.GeneroViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneroViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkbox, parent, false)
            return GeneroViewHolder(view)
        }

        override fun onBindViewHolder(holder: GeneroViewHolder, position: Int) {
            val genero = generosList[position]
            holder.bind(genero, selectedGeneros.contains(genero))
        }

        override fun getItemCount(): Int = generosList.size

        inner class GeneroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxItem)

            fun bind(genero: String, isChecked: Boolean) {
                checkBox.text = genero
                checkBox.isChecked = isChecked

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onGeneroChecked(genero, isChecked)
                }
            }
        }
    }


        class OndeAssistirAdapter(
            private val plataformasList: List<Plataforma>,
            private val selectedPlataformas: List<String>,
            private val onPlataformaChecked: (Plataforma, Boolean) -> Unit,
            private val isDetalhesFragment: Boolean
        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val layoutId = if (isDetalhesFragment) {
                    R.layout.item_plataforma_detalhes
                } else {
                    R.layout.item_plataforma
                }
                val view = LayoutInflater.from(parent.context)
                    .inflate(layoutId, parent, false)
                return if (isDetalhesFragment) {
                    DetalhesPlataformaViewHolder(view)
                } else {
                    PlataformaViewHolder(view)
                }
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val plataforma = plataformasList[position]
                if (holder is PlataformaViewHolder) {
                    holder.bind(plataforma, selectedPlataformas.contains(plataforma.nome))
                } else if (holder is DetalhesPlataformaViewHolder) {
                    holder.bind(plataforma)
                }
            }

            override fun getItemCount(): Int = plataformasList.size

            inner class PlataformaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val iconeImageView: ImageView = itemView.findViewById(R.id.iconePlataforma)
                private val nomeTextView: TextView = itemView.findViewById(R.id.nomePlataforma)
                private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxItem)

                fun bind(plataforma: Plataforma, isChecked: Boolean) {
                    nomeTextView.text = plataforma.nome
                    checkBox.isChecked = isChecked

                    Glide.with(itemView.context)
                        .load(plataforma.iconeUrl)
                        .into(iconeImageView)

                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        onPlataformaChecked(plataforma, isChecked)
                    }
                }
            }
            inner class DetalhesPlataformaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val iconeImageView: ImageView = itemView.findViewById(R.id.iconePlataforma)

                fun bind(plataforma: Constants.Plataforma) {
                    Glide.with(itemView.context)
                        .load(plataforma.iconeUrl)
                        .into(iconeImageView)

                    iconeImageView.setOnClickListener {
                        if (plataforma.link.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(plataforma.link))
                            itemView.context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                itemView.context,
                                "Link não disponível para esta plataforma.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        }





    class MoviesListAdapter(
        private val onItemClick: (Movie) -> Unit,
        private val onFavoriteClick: ((String, Boolean) -> Unit)? = null,
        private val layoutId: Int = R.layout.item_movie_card,
        var favoriteMovies: List<String> = emptyList()
    ) : ListAdapter<Movie, MoviesListAdapter.MovieViewHolder>(MovieDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return MovieViewHolder(view, onItemClick, onFavoriteClick)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = getItem(position)
            val isFavorite = favoriteMovies.contains(movie.id)
            holder.bind(movie, isFavorite)
        }

        class MovieViewHolder(
            itemView: View,
            private val onItemClick: (Movie) -> Unit,
            private val onFavoriteClick: ((String, Boolean) -> Unit)?
        ) : RecyclerView.ViewHolder(itemView) {
            private val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
            private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
            private val movieRating: TextView = itemView.findViewById(R.id.movieRating)
            private val movieFavorite: ImageView? = itemView.findViewById(R.id.movieFavorite)

            fun bind(movie: Movie, isFavorite: Boolean) {
                movieTitle.text = movie.titulo
                movieRating.text = movie.mediaAvaliacao.toString()

                Glide.with(itemView.context)
                    .load(movie.imagemUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(movieImage)

                itemView.setOnClickListener {
                    onItemClick(movie)
                }

                movieFavorite?.apply {
                    val favoriteIcon = if (isFavorite) {
                        R.drawable.ic_favorite
                    } else {
                        R.drawable.ic_favorite_lesser
                    }
                    setImageResource(favoriteIcon)

                    setOnClickListener {
                        onFavoriteClick?.invoke(movie.id, !isFavorite)
                    }
                }
            }
        }

        class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

}


