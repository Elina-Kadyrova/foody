package com.itis.foody.features.recipe.domain.repositories

import com.itis.foody.features.recipe.domain.models.RecipeSimple

interface RecipeSearchRepository {
    suspend fun getRecipeListByQuery(query: String): MutableList<RecipeSimple>
    suspend fun getLastSeenRecipes(): MutableList<RecipeSimple>
}
