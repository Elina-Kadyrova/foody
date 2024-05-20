package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import javax.inject.Inject

class RecipeDetailsToRecipeSimpleMapper @Inject constructor() :
    ModelMapper<RecipeDetails, RecipeSimple> {

    override fun map(source: RecipeDetails): RecipeSimple =
        RecipeSimple(
            source.id,
            source.image,
            source.title
        )
}
