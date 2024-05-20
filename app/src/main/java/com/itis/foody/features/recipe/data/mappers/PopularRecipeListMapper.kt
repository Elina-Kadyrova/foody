package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.features.recipe.data.response.popularRecipes.PopularRecipesResponse
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import javax.inject.Inject

class PopularRecipeListMapper @Inject constructor() :
    ModelMapper<PopularRecipesResponse, MutableList<RecipeSimple>> {

    override fun map(source: PopularRecipesResponse): MutableList<RecipeSimple> {
        val list = mutableListOf<RecipeSimple>()
        source.recipes?.forEach {
            list.add(
                RecipeSimple(it.id, it.image, it.title)
            )
        }
        return list
    }
}
