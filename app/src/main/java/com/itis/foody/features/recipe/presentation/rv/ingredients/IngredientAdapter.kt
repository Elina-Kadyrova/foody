package com.itis.foody.features.recipe.presentation.rv.ingredients

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.foody.features.recipe.domain.models.Ingredient
import com.itis.foody.features.recipe.domain.utils.RecipeDataConverter

class IngredientAdapter(
    private val list: List<Ingredient>,
    private val converter: RecipeDataConverter
) : RecyclerView.Adapter<IngredientHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder =
        IngredientHolder.create(parent, converter)

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}
