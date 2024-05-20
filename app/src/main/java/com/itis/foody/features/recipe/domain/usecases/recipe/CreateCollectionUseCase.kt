package com.itis.foody.features.recipe.domain.usecases.recipe

import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateCollectionUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(name: String): MutableList<RecipeCollection> {
        return withContext(dispatcher) {
            recipeRepository.createRecipeCollection(name)
        }
    }
}
