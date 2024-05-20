package com.itis.foody.features.recipe.presentation.rv.popularRecipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.foody.databinding.ItemCollectionBinding
import com.itis.foody.features.recipe.domain.models.PopularRecipeTag

class PopularRecipesHolder(
    private val binding: ItemCollectionBinding,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PopularRecipeTag) {
        with(binding) {
            ivImage.load(item.image)
        }
        itemView.setOnClickListener {
            action(item.tag)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = PopularRecipesHolder(
            ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }
}
