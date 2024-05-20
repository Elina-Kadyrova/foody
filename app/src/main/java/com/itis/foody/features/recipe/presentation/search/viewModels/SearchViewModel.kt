package com.itis.foody.features.recipe.presentation.search.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.usecases.search.GetLastSeenRecipesUseCase
import com.itis.foody.features.recipe.domain.usecases.search.GetRecipeListByQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getLastSeenRecipesUseCase: GetLastSeenRecipesUseCase,
    private val getRecipeListByQueryUseCase: GetRecipeListByQueryUseCase
) : ViewModel() {

    private var _recipes: MutableLiveData<Result<MutableList<RecipeSimple>>> =
        MutableLiveData()
    val recipes: LiveData<Result<MutableList<RecipeSimple>>> = _recipes

    private var _lastSeen: MutableLiveData<Result<MutableList<RecipeSimple>>> =
        MutableLiveData()
    val lastSeen: LiveData<Result<MutableList<RecipeSimple>>> = _lastSeen

    fun loadLastSeen() {
        viewModelScope.launch {
            kotlin.runCatching {
                getLastSeenRecipesUseCase()
            }.onSuccess {
                _lastSeen.value = Result.success(it)
            }.onFailure {
                _lastSeen.value = Result.failure(it)
            }
        }
    }

    fun getRecipesByQuery(query: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                getRecipeListByQueryUseCase(query)
            }.onSuccess {
                _recipes.value = Result.success(it)
            }.onFailure {
                _recipes.value = Result.failure(it)
            }
        }
    }
}
