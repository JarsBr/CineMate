package com.cinemate.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cinemate.app.R
import com.cinemateapp.utils.Constants.Plataforma

class MoviesAdapter {

    class GenerosAdapter(
        private val generosList: List<String>,
        private val onGeneroChecked: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<GenerosAdapter.GeneroViewHolder>() {

        private val checkedItems = mutableSetOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneroViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkbox, parent, false)
            return GeneroViewHolder(view)
        }

        override fun onBindViewHolder(holder: GeneroViewHolder, position: Int) {
            val genero = generosList[position]
            holder.bind(genero)
        }

        override fun getItemCount(): Int = generosList.size

        inner class GeneroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxItem)

            fun bind(genero: String) {
                checkBox.text = genero
                checkBox.isChecked = checkedItems.contains(genero)

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        checkedItems.add(genero)
                    } else {
                        checkedItems.remove(genero)
                    }
                    onGeneroChecked(genero, isChecked)
                }
            }
        }
    }

    class OndeAssistirAdapter(
        private val plataformasList: List<Plataforma>,
        private val onPlataformaChecked: (Plataforma, Boolean) -> Unit
    ) : RecyclerView.Adapter<OndeAssistirAdapter.PlataformaViewHolder>() {

        private val checkedItems = mutableSetOf<Plataforma>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlataformaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_plataforma, parent, false)
            return PlataformaViewHolder(view)
        }

        override fun onBindViewHolder(holder: PlataformaViewHolder, position: Int) {
            val plataforma = plataformasList[position]
            holder.bind(plataforma)
        }

        override fun getItemCount(): Int = plataformasList.size

        inner class PlataformaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val iconeImageView: ImageView = itemView.findViewById(R.id.iconePlataforma)
            private val nomeTextView: TextView = itemView.findViewById(R.id.nomePlataforma)
            private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxItem)

            fun bind(plataforma: Plataforma) {
                nomeTextView.text = plataforma.nome
                checkBox.isChecked = checkedItems.contains(plataforma)

                Glide.with(itemView.context)
                    .load(plataforma.iconeUrl)
                    .into(iconeImageView)

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        checkedItems.add(plataforma)
                    } else {
                        checkedItems.remove(plataforma)
                    }
                    onPlataformaChecked(plataforma, isChecked)
                }
            }
        }
    }
}
