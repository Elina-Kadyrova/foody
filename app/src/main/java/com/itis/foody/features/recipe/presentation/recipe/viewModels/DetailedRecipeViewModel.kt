package com.itis.foody.features.recipe.presentation.recipe.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.common.utils.SingleLiveEvent
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.usecases.recipe.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedRecipeViewModel @Inject constructor(
    private val getRecipeInfoUseCase: GetRecipeInfoUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val updateLastSeenUseCase: UpdateLastSeenUseCase,
    private val removeRecipeUseCase: RemoveRecipeUseCase,
    private val getUserCollectionsUseCase: GetUserCollectionsUseCase,
    private val createCollectionForSavingRecipeUseCase: CreateCollectionForSavingRecipesUseCase,
    private val checkIfRecipeAlreadySavedUseCase: CheckIfRecipeAlreadySavedUseCase
) : ViewModel() {

    private var _recipeInfo: SingleLiveEvent<Result<RecipeDetails>> = SingleLiveEvent()
    val recipeInfo: SingleLiveEvent<Result<RecipeDetails>> = _recipeInfo

    private var _savedRecipe: SingleLiveEvent<Result<Recipe>> = SingleLiveEvent()
    val savedRecipe: SingleLiveEvent<Result<Recipe>> = _savedRecipe

    private var _removedRecipe: SingleLiveEvent<Result<Recipe>> = SingleLiveEvent()
    val removedRecipe: SingleLiveEvent<Result<Recipe>> = _removedRecipe

    private var _isSaved: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val isSaved: LiveData<Result<Boolean>> = _isSaved

    private var _userCollections: MutableLiveData<Result<MutableList<RecipeCollection>>> =
        MutableLiveData()
    val userCollections: LiveData<Result<MutableList<RecipeCollection>>> = _userCollections

    private var _newCollection: SingleLiveEvent<Result<RecipeCollection>> = SingleLiveEvent()
    val newCollection: SingleLiveEvent<Result<RecipeCollection>> = _newCollection

    private var _lastSeen: MutableLiveData<Result<MutableList<RecipeSimple>?>> =
        MutableLiveData()
    val lastSeen: LiveData<Result<MutableList<RecipeSimple>?>> = _lastSeen

    fun getRecipeInfo(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                getRecipeInfoUseCase(id)
            }.onSuccess {
                _recipeInfo.value = Result.success(it)
            }.onFailure {
                _recipeInfo.value = Result.failure(it)
            }
        }
    }

    fun saveRecipe(recipe: RecipeDetails, recipeSetId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                saveRecipeUseCase(recipe, recipeSetId)
            }.onSuccess {
                _savedRecipe.value = Result.success(it)
            }.onFailure {
                _savedRecipe.value = Result.failure(it)
            }
        }
    }

    fun deleteFromSaved(recipeId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                removeRecipeUseCase(recipeId)
            }.onSuccess {
                _removedRecipe.value = Result.success(it)
            }.onFailure {
                _removedRecipe.value = Result.failure(it)
            }
        }
    }

    fun checkIfRecipeAlreadySaved(recipeId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                checkIfRecipeAlreadySavedUseCase(recipeId)
            }.onSuccess {
                _isSaved.value = Result.success(it)
            }.onFailure {
                _isSaved.value = Result.failure(it)
            }
        }
    }

    fun getUserCollections() {
        viewModelScope.launch {
            kotlin.runCatching {
                getUserCollectionsUseCase()
            }.onSuccess {
                _userCollections.value = Result.success(it)
            }.onFailure {
                _userCollections.value = Result.failure(it)
            }
        }
    }

    fun createCollection(name: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                createCollectionForSavingRecipeUseCase(name)
            }.onSuccess {
                _newCollection.value = Result.success(it)
            }.onFailure {
                _newCollection.value = Result.failure(it)
            }
        }
    }

    fun updateLastSeen(recipeSimple: RecipeDetails) {
        viewModelScope.launch {
            kotlin.runCatching {
                updateLastSeenUseCase(recipeSimple)
            }.onSuccess {
                _lastSeen.value = Result.success(it)
            }.onFailure {
                _lastSeen.value = Result.failure(it)
            }
        }
    }
}
