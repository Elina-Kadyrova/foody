package com.itis.foody.features.recipe.presentation.rv.recipeSteps

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.foody.databinding.ItemRecipeStepBinding
import com.itis.foody.features.recipe.domain.models.RecipeStep

class RecipeStepHolder(
    private val binding: ItemRecipeStepBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RecipeStep) {
        with(binding) {
            tvStepNum.text = "${item.number}."
            tvRecipeStepDesc.text = Html.fromHtml(item.desc, Html.FROM_HTML_MODE_COMPACT)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = RecipeStepHolder(
            ItemRecipeStepBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
