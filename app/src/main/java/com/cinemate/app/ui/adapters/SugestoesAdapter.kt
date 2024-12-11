package com.cinemate.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cinemate.app.data.models.Suggestion
import com.cinemate.app.databinding.ItemSuggestionBinding

class SugestoesAdapter(private val suggestions: List<Suggestion>) :
    RecyclerView.Adapter<SugestoesAdapter.SugestaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugestaoViewHolder {
        val binding = ItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SugestaoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SugestaoViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.bind(suggestion)
    }

    override fun getItemCount(): Int = suggestions.size

    class SugestaoViewHolder(private val binding: ItemSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: Suggestion) {
            binding.suggestionText.text = suggestion.suggestionText
        }
    }
}
