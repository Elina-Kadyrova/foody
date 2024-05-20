package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.features.recipe.data.response.similarRecipes.SimilarRecipesResponse
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.utils.RecipeDataConverter
import javax.inject.Inject

class SimilarRecipeListMapper @Inject constructor() :
    ModelMapper<SimilarRecipesResponse, MutableList<RecipeSimple>> {

    @Inject
    lateinit var imageUrlBuilder: RecipeDataConverter

    override fun map(source: SimilarRecipesResponse): MutableList<RecipeSimple> {
        val list = mutableListOf<RecipeSimple>()
        source.forEach {
            list.add(
                RecipeSimple(it.id, imageUrlBuilder.convertRecipeImageUrl(it.id), it.title)
            )
        }
        return list
    }
}
