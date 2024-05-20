package com.itis.foody.features.recipe.presentation.recipe.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.usecases.recipe.CreateCollectionUseCase
import com.itis.foody.features.recipe.domain.usecases.recipe.GetUserCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSetViewModel @Inject constructor(
    private val getUserCollectionsUseCase: GetUserCollectionsUseCase,
    private val createCollectionUseCase: CreateCollectionUseCase
) : ViewModel() {

    private var _newCollection: MutableLiveData<Result<MutableList<RecipeCollection>>> =
        MutableLiveData()
    val newCollection: LiveData<Result<MutableList<RecipeCollection>>> = _newCollection

    private var _userCollection: MutableLiveData<Result<MutableList<RecipeCollection>>> =
        MutableLiveData()
    val userCollection: LiveData<Result<MutableList<RecipeCollection>>> = _userCollection

    fun getCollections() {
        viewModelScope.launch {
            kotlin.runCatching {
                getUserCollectionsUseCase()
            }.onSuccess {
                _userCollection.value = Result.success(it)
            }.onFailure {
                _userCollection.value = Result.failure(it)
            }
        }
    }

    fun createNewSet(name: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                createCollectionUseCase(name)
            }.onSuccess {
                _newCollection.value = Result.success(it)
            }.onFailure {
                _newCollection.value = Result.failure(it)
            }
        }
    }
}
