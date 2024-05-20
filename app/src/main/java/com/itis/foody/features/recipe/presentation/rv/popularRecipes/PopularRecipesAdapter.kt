package com.itis.foody.features.recipe.presentation.rv.popularRecipes

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.foody.features.recipe.domain.models.PopularRecipeTag

class PopularRecipesAdapter(
    private val list: List<PopularRecipeTag>,
    private val action: (String) -> Unit
) : RecyclerView.Adapter<PopularRecipesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularRecipesHolder =
        PopularRecipesHolder.create(parent, action)

    override fun onBindViewHolder(holder: PopularRecipesHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}
