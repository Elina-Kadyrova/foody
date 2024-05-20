package com.itis.foody.features.recipe.domain.usecases.recipe

import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecipeInfoUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(id: Int): RecipeDetails {
        return withContext(dispatcher) {
            recipeRepository.getRecipeInfoById(id)
        }
    }
}
