package com.cinemate.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinemate.app.R
import com.cinemateapp.utils.Constants.Plataforma
import androidx.recyclerview.widget.ListAdapter
import com.cinemate.app.data.models.Movie

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
        private val onPlataformaChecked: (Plataforma, Boolean) -> Unit
    ) : RecyclerView.Adapter<OndeAssistirAdapter.PlataformaViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlataformaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_plataforma, parent, false)
            return PlataformaViewHolder(view)
        }

        override fun onBindViewHolder(holder: PlataformaViewHolder, position: Int) {
            val plataforma = plataformasList[position]
            holder.bind(plataforma, selectedPlataformas.contains(plataforma.nome))
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
    }



    class MoviesListAdapter(private val onItemClick: (Movie) -> Unit) :
        ListAdapter<Movie, MoviesListAdapter.MovieViewHolder>(MovieDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_card, parent, false)
            return MovieViewHolder(view, onItemClick)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            val movie = getItem(position)
            holder.bind(movie)
        }

        class MovieViewHolder(itemView: View, private val onItemClick: (Movie) -> Unit) :
            RecyclerView.ViewHolder(itemView) {
            private val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
            private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
            private val movieRating: TextView = itemView.findViewById(R.id.movieRating)

            fun bind(movie: Movie) {
                movieTitle.text = movie.titulo
                movieRating.text = movie.mediaAvaliacao.toString()

                Glide.with(itemView.context)
                    .load(movie.imagemUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(movieImage)

                itemView.setOnClickListener {
                    onItemClick(movie)
                }
            }
        }



    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.titulo == newItem.titulo
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}


