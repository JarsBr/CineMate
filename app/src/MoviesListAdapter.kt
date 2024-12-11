class MoviesListAdapter(
    private val onItemClick: (Movie) -> Unit,
    private val onFavoriteClick: ((String, Boolean) -> Unit)? = null, // Callback para favoritos opcional
    private val layoutId: Int = R.layout.item_movie_card, // Layout padrão
    var favoriteMovies: List<String> = emptyList() // Lista de favoritos opcional
) : ListAdapter<Movie, MoviesListAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MovieViewHolder(view, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        val isFavorite = favoriteMovies.contains(movie.id) // Verifica se o filme está nos favoritos
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

            // Atualiza o ícone de favorito com base no estado
            movieFavorite?.apply {
                val favoriteIcon = if (isFavorite) {
                    R.drawable.ic_favorite
                } else {
                    R.drawable.ic_favorite_lesser
                }
                setImageResource(favoriteIcon)

                // Configura o clique no ícone de favorito
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
