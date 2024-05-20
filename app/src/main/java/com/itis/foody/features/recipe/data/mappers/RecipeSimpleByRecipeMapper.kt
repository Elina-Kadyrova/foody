package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import javax.inject.Inject

class RecipeSimpleByRecipeMapper @Inject constructor() : ModelMapper<Recipe, RecipeSimple> {

    override fun map(source: Recipe): RecipeSimple =
        RecipeSimple(
            source.apiId,
            source.image,
            source.title
        )
}
