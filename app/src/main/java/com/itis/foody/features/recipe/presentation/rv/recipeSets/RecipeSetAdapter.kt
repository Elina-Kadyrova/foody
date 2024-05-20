package com.itis.foody.features.recipe.presentation.rv.recipeSets

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.itis.foody.features.recipe.domain.models.RecipeCollection

class RecipeSetAdapter(
    private val action: (String, String) -> Unit
) : ListAdapter<RecipeCollection, RecipeSetHolder>(RecipeDiffUtilsCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeSetHolder = RecipeSetHolder.create(parent, action)

    override fun onBindViewHolder(holder: RecipeSetHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<RecipeCollection>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }
}

class RecipeDiffUtilsCallback : DiffUtil.ItemCallback<RecipeCollection>() {

    override fun areItemsTheSame(
        oldItem: RecipeCollection,
        newItem: RecipeCollection
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: RecipeCollection,
        newItem: RecipeCollection
    ): Boolean = oldItem == newItem
}
