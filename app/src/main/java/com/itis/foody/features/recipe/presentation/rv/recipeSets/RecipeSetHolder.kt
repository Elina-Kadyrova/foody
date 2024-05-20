package com.itis.foody.features.recipe.presentation.rv.recipeSets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.foody.R
import com.itis.foody.databinding.ItemRecipeBinding
import com.itis.foody.features.recipe.domain.models.RecipeCollection

class RecipeSetHolder(
    private val binding: ItemRecipeBinding,
    private val action: (String, String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecipeCollection) {
        with(binding) {
            tvName.text = item.name
            ivImage.load(R.drawable.recipe_set_default)
        }
        itemView.setOnClickListener {
            action(item.id, item.name)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String, String) -> Unit
        ) = RecipeSetHolder(
            ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }
}
