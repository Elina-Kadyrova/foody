package com.itis.foody.features.recipe.presentation.rv.recipes

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.itis.foody.features.recipe.domain.models.RecipeSimple

class RecipeAdapter(
    private val action: (Int) -> Unit
) : ListAdapter<RecipeSimple, RecipeHolder>(RecipeDiffUtilsCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeHolder = RecipeHolder.create(parent, action)

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<RecipeSimple>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }
}

class RecipeDiffUtilsCallback : DiffUtil.ItemCallback<RecipeSimple>() {

    override fun areItemsTheSame(
        oldItem: RecipeSimple,
        newItem: RecipeSimple
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: RecipeSimple,
        newItem: RecipeSimple
    ): Boolean = oldItem == newItem
}
