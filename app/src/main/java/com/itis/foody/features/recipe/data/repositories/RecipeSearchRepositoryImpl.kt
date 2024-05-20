package com.itis.foody.features.recipe.data.repositories

import com.itis.foody.common.db.searchStory.LastSeenRepository
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.data.api.Api
import com.itis.foody.features.recipe.data.response.searchByIngredient.RecipeListByIngredientResponse
import com.itis.foody.features.recipe.data.response.searchByName.RecipeListByNameResponse
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.repositories.RecipeSearchRepository
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    private val api: Api,
    private val recipeListByIngredientMapper: ModelMapper<RecipeListByIngredientResponse, MutableList<RecipeSimple>>,
    private val recipeListByNameMapper: ModelMapper<RecipeListByNameResponse, MutableList<RecipeSimple>>
) : RecipeSearchRepository {

    override suspend fun getRecipeListByQuery(query: String): MutableList<RecipeSimple> {
        val listByName = getRecipeListByName(query)
        val listByIngredient = getRecipeListByIngredient(query)
        return mapResults(listByName, listByIngredient)
    }

    override suspend fun getLastSeenRecipes(): MutableList<RecipeSimple> =
        LastSeenRepository.getList()

    private fun mapResults(
        firstList: MutableList<RecipeSimple>,
        secondList: MutableList<RecipeSimple>
    ): MutableList<RecipeSimple> {
        firstList.addAll(secondList)
        firstList.shuffle()
        return firstList
    }

    private suspend fun getRecipeListByIngredient(ingredient: String): MutableList<RecipeSimple> =
        recipeListByIngredientMapper.map(
            api.getRecipesByIngredients(ingredient)
        )

    private suspend fun getRecipeListByName(name: String): MutableList<RecipeSimple> =
        recipeListByNameMapper.map(
            api.getRecipesByName(name)
        )
}
