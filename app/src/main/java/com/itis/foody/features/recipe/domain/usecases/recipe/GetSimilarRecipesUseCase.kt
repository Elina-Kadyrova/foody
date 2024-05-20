package com.itis.foody.features.recipe.domain.usecases.recipe

import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSimilarRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: Int): MutableList<RecipeSimple> {
        return withContext(dispatcher) {
            recipeRepository.getSimilarRecipesById(id)
        }
    }
}
