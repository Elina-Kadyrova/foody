package com.itis.foody.features.recipe.domain.usecases.recipe

import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: Int): Recipe {
        return withContext(dispatcher) {
            recipeRepository.deleteRecipeFromCollection(id)
        }
    }
}
