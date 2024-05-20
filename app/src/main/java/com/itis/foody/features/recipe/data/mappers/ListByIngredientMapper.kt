package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.features.recipe.data.response.searchByIngredient.RecipeListByIngredientResponse
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import javax.inject.Inject

class ListByIngredientMapper @Inject constructor() :
    ModelMapper<RecipeListByIngredientResponse, MutableList<RecipeSimple>> {

    override fun map(source: RecipeListByIngredientResponse): MutableList<RecipeSimple> {
        val list = mutableListOf<RecipeSimple>()
        source.forEach {
            list.add(
                RecipeSimple(it.id, it.image, it.title)
            )
        }
        return list
    }
}
