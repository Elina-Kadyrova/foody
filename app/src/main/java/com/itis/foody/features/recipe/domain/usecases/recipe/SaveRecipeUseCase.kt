package com.itis.foody.features.recipe.domain.usecases.recipe

import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(recipe: RecipeDetails, recipeSetId: String): Recipe {
        return withContext(dispatcher) {
            recipeRepository.saveRecipeToCollection(recipe, recipeSetId)
        }
    }
}
