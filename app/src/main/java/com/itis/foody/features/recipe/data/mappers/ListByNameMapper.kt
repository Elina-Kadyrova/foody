package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.features.recipe.data.response.searchByName.RecipeListByNameResponse
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import javax.inject.Inject

class ListByNameMapper @Inject constructor() :
    ModelMapper<RecipeListByNameResponse, MutableList<RecipeSimple>> {

    override fun map(source: RecipeListByNameResponse): MutableList<RecipeSimple> {
        val list = mutableListOf<RecipeSimple>()
        source.results?.forEach {
            list.add(
                RecipeSimple(it.id, it.image, it.title)
            )
        }
        return list
    }
}
