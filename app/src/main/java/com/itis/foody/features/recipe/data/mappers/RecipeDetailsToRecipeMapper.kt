package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import javax.inject.Inject

class RecipeDetailsToRecipeMapper @Inject constructor() : ModelMapper<RecipeDetails, Recipe> {

    override fun map(source: RecipeDetails): Recipe =
        Recipe(
            source.id,
            source.title,
            source.image,
        )
}
