package com.cinemate.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinemate.app.data.models.Suggestion
import com.cinemate.app.databinding.ItemSuggestionBinding

class SuggestionAdapter(
    private val suggestions: List<Suggestion>,
    private val onDeleteClick: (String) -> Unit // Callback para exclusão
) : RecyclerView.Adapter<SuggestionAdapter.SugestaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugestaoViewHolder {
        val binding = ItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SugestaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SugestaoViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.bind(suggestion, onDeleteClick)
    }

    override fun getItemCount(): Int = suggestions.size

    class SugestaoViewHolder(private val binding: ItemSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: Suggestion, onDeleteClick: (String) -> Unit) {
            binding.suggestionText.text = suggestion.suggestionText

            // Configurando o botão de deletar
            binding.btnDeleteSuggestion.setOnClickListener {
                onDeleteClick(suggestion.id) // Chama o callback passando o ID da sugestão
            }
        }
    }
}
