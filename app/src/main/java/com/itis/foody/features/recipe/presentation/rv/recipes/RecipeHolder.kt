package com.itis.foody.features.recipe.presentation.rv.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.foody.databinding.ItemRecipeBinding
import com.itis.foody.features.recipe.domain.models.RecipeSimple

class RecipeHolder(
    private val binding: ItemRecipeBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecipeSimple) {
        with(binding) {
            tvName.text = item.title
            ivImage.load(item.image)
        }
        itemView.setOnClickListener {
            action(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (Int) -> Unit
        ) = RecipeHolder(
            ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }
}
