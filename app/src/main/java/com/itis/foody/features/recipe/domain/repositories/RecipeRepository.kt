package com.itis.foody.features.recipe.domain.repositories

import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeSimple

interface RecipeRepository {
    suspend fun getRecipeInfoById(id: Int): RecipeDetails
    suspend fun getPopularRecipesByTag(tag: String): MutableList<RecipeSimple>
    suspend fun getSimilarRecipesById(id: Int): MutableList<RecipeSimple>

    suspend fun createRecipeCollection(name: String): MutableList<RecipeCollection>
    suspend fun removeRecipeCollection(id: String): MutableList<RecipeCollection>

    suspend fun getUserRecipeCollections(): MutableList<RecipeCollection>
    suspend fun createRecipeCollectionForSaving(name: String): RecipeCollection
    suspend fun getRecipesByCollectionId(recipeSetId: String): MutableList<RecipeSimple>

    suspend fun saveRecipeToCollection(recipe: RecipeDetails, recipeSetId: String): Recipe
    suspend fun deleteRecipeFromCollection(recipeId: Int): Recipe

    suspend fun checkIfRecipeAlreadySaved(recipeId: Int): Boolean

    suspend fun updateLastSeen(recipe: RecipeDetails): MutableList<RecipeSimple>?
}
