package com.itis.foody.features.recipe.presentation.rv.recipeSteps

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.foody.features.recipe.domain.models.RecipeStep

class RecipeStepAdapter(
    private val list: List<RecipeStep>
) : RecyclerView.Adapter<RecipeStepHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeStepHolder =
        RecipeStepHolder.create(parent)

    override fun onBindViewHolder(holder: RecipeStepHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}
