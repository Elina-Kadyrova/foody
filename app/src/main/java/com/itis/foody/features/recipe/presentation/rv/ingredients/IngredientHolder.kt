package com.itis.foody.features.recipe.presentation.rv.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.foody.databinding.ItemIngredientBinding
import com.itis.foody.features.recipe.domain.models.Ingredient
import com.itis.foody.features.recipe.domain.utils.RecipeDataConverter

class IngredientHolder(
    private val binding: ItemIngredientBinding,
    private val converter: RecipeDataConverter,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Ingredient) {
        with(binding) {
            tvIngredientName.text = item.name
            tvIngredientAmount.text =
                converter.convertIngredientAmountToString(item.amount, item.unit)
            ivImage.load(converter.convertIngredientImageUrl(item.image))
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageUrlBuilder: RecipeDataConverter
        ) = IngredientHolder(
            ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            imageUrlBuilder
        )
    }
}
