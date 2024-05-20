package com.itis.foody.features.recipe.domain.usecases.search

import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.repositories.RecipeSearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecipeListByQueryUseCase @Inject constructor(
    private val recipeSearchRepository: RecipeSearchRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(query: String): MutableList<RecipeSimple> {
        return withContext(dispatcher) {
            recipeSearchRepository.getRecipeListByQuery(query)
        }
    }
}
