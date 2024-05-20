package com.itis.foody.features.recipe.presentation.recipe.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.foody.common.utils.SingleLiveEvent
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.usecases.recipe.GetSavedRecipesUseCase
import com.itis.foody.features.recipe.domain.usecases.recipe.RemoveCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val getSavedRecipesUseCase: GetSavedRecipesUseCase,
    private val removeCollectionUseCase: RemoveCollectionUseCase
) : ViewModel() {

    private var _savedRecipes: SingleLiveEvent<Result<MutableList<RecipeSimple>>> =
        SingleLiveEvent()
    val savedRecipes: SingleLiveEvent<Result<MutableList<RecipeSimple>>> = _savedRecipes

    private var _removedCollection: SingleLiveEvent<Result<MutableList<RecipeCollection>>> =
        SingleLiveEvent()
    val removedCollection: SingleLiveEvent<Result<MutableList<RecipeCollection>>> =
        _removedCollection

    fun getSavedRecipes(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                getSavedRecipesUseCase(id)
            }.onSuccess {
                _savedRecipes.value = Result.success(it)
            }.onFailure {
                _savedRecipes.value = Result.failure(it)
            }
        }
    }

    fun removeCollection(collectionId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                removeCollectionUseCase(collectionId)
            }.onSuccess {
                _removedCollection.value = Result.success(it)
            }.onFailure {
                _removedCollection.value = Result.failure(it)
            }
        }
    }
}
