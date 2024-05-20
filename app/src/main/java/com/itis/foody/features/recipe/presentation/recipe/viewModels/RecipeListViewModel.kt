package com.itis.foody.features.recipe.presentation.recipe.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.foody.common.utils.SingleLiveEvent
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.usecases.recipe.GetPopularRecipesByTagUseCase
import com.itis.foody.features.recipe.domain.usecases.recipe.GetSimilarRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getPopularRecipesByTagUseCase: GetPopularRecipesByTagUseCase,
    private val getSimilarRecipesUseCase: GetSimilarRecipesUseCase
) : ViewModel() {

    private var _popularRecipes: SingleLiveEvent<Result<MutableList<RecipeSimple>>> =
        SingleLiveEvent()
    val popularRecipes: SingleLiveEvent<Result<MutableList<RecipeSimple>>> = _popularRecipes

    private var _similarRecipes: SingleLiveEvent<Result<MutableList<RecipeSimple>>> =
        SingleLiveEvent()
    val similarRecipes: SingleLiveEvent<Result<MutableList<RecipeSimple>>> = _similarRecipes

    fun getPopularRecipes(tag: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                getPopularRecipesByTagUseCase(tag)
            }.onSuccess {
                _popularRecipes.value = Result.success(it)
            }.onFailure {
                _popularRecipes.value = Result.failure(it)
            }
        }
    }

    fun getSimilarRecipes(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                getSimilarRecipesUseCase(id)
            }.onSuccess {
                _similarRecipes.value = Result.success(it)
            }.onFailure {
                _similarRecipes.value = Result.failure(it)
            }
        }
    }
}
