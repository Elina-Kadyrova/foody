package com.itis.foody.features.recipe.domain.tempData

import com.itis.foody.R
import com.itis.foody.features.recipe.domain.models.PopularRecipeTag

object RecipeTagRepository {
    var id = 0

    val recipeTags = arrayListOf(
        PopularRecipeTag(
            id++,
            "vegetarian",
            R.drawable.vegetarian
        ),
        PopularRecipeTag(
            id++,
            "dessert",
            R.drawable.dessert
        ),
        PopularRecipeTag(
            id++,
            "breakfast",
            R.drawable.breakfast
        ),
        PopularRecipeTag(
            id++,
            "dinner",
            R.drawable.dinner
        ),
        PopularRecipeTag(
            id++,
            "lunch",
            R.drawable.lunch
        )
    )
}
