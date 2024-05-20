package com.itis.foody.features.recipe.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.common.db.entities.RecipeSet
import com.itis.foody.common.db.entities.User
import com.itis.foody.common.db.searchStory.LastSeenRepository
import com.itis.foody.common.di.modules.qualifiers.RecipeSetsReference
import com.itis.foody.common.di.modules.qualifiers.RecipesReference
import com.itis.foody.common.di.modules.qualifiers.UsersReference
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.data.api.Api
import com.itis.foody.features.recipe.data.response.popularRecipes.PopularRecipesResponse
import com.itis.foody.features.recipe.data.response.recipeInfo.RecipeInfoResponse
import com.itis.foody.features.recipe.data.response.similarRecipes.SimilarRecipesResponse
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import com.itis.foody.features.user.domain.exceptions.UserNotFoundException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RecipeRepositoryImpl @Inject constructor(
    private val api: Api,
    private val auth: FirebaseAuth,
    private val recipeInfoMapper: ModelMapper<RecipeInfoResponse, RecipeDetails>,
    private val similarRecipesMapper: ModelMapper<SimilarRecipesResponse, MutableList<RecipeSimple>>,
    private val popularRecipesMapper: ModelMapper<PopularRecipesResponse, MutableList<RecipeSimple>>,
    private val detailedToSimpleMapper: ModelMapper<RecipeDetails, RecipeSimple>,
    private val detailedToRecipeMapper: ModelMapper<RecipeDetails, Recipe>,
    private val recipeSetToCollectionMapper: ModelMapper<RecipeSet, RecipeCollection>,
    private val recipeToSimpleRecipeMapper: ModelMapper<Recipe, RecipeSimple>,
    @RecipesReference private val recipesReference: DatabaseReference,
    @RecipeSetsReference private val recipeSetReference: DatabaseReference,
    @UsersReference private val usersReference: DatabaseReference
) : RecipeRepository {

    override suspend fun getUserRecipeCollections(): MutableList<RecipeCollection> =
        getUserCollections(getCollectionIds())

    override suspend fun createRecipeCollectionForSaving(name: String): RecipeCollection =
        recipeSetToCollectionMapper.map(addCollection(name))

    override suspend fun getRecipesByCollectionId(recipeSetId: String): MutableList<RecipeSimple> {
        val recipeIdList = getRecipeIdList(recipeSetId)
        return getRecipes(recipeIdList)
    }

    override suspend fun saveRecipeToCollection(
        recipe: RecipeDetails,
        recipeSetId: String
    ): Recipe {
        val savedRecipe = saveRecipe(recipe)
        addRecipeToCollection(recipeSetId, savedRecipe.apiId)
        return savedRecipe
    }

    override suspend fun deleteRecipeFromCollection(recipeId: Int): Recipe {
        val recipe = getRecipe(recipeId)
        deleteRecipe(recipeId)
        deleteFromCollection(recipeId)
        return recipe
    }

    override suspend fun checkIfRecipeAlreadySaved(recipeId: Int): Boolean {
        val collections = getUserRecipeCollections()
        var isSaved = false
        collections.forEach {
            val recipeSet = getRecipeSet(it.id)
            if (recipeSet.recipes.containsKey(recipeId.toString())) {
                isSaved = true
                return@forEach
            }
        }
        return isSaved
    }

    override suspend fun createRecipeCollection(name: String): MutableList<RecipeCollection> {
        addCollection(name)
        return getUserRecipeCollections()
    }

    override suspend fun removeRecipeCollection(id: String): MutableList<RecipeCollection> {
        removeRecipeSet(id)
        removeRecipeSetFromUser(id)
        return getUserRecipeCollections()
    }

    override suspend fun updateLastSeen(recipe: RecipeDetails): MutableList<RecipeSimple> {
        LastSeenRepository.addNew(
            detailedToSimpleMapper.map(recipe)
        )
        return LastSeenRepository.getList()
    }

    override suspend fun getRecipeInfoById(id: Int): RecipeDetails =
        recipeInfoMapper.map(
            api.getRecipeInfo(id)
        )

    override suspend fun getPopularRecipesByTag(tag: String): MutableList<RecipeSimple> =
        popularRecipesMapper.map(
            api.getPopularRecipesByTag(tag)
        )

    override suspend fun getSimilarRecipesById(id: Int): MutableList<RecipeSimple> =
        similarRecipesMapper.map(
            api.getSimilarRecipes(id)
        )

    private suspend fun getRecipe(recipeId: Int): Recipe = suspendCoroutine {
        recipesReference.child(recipeId.toString()).get()
            .addOnSuccessListener { data ->
                data.getValue(Recipe::class.java)?.apply {
                    it.resume(this)
                }
            }
    }

    private suspend fun deleteFromCollection(recipeId: Int) {
        val collections = getUserRecipeCollections()
        collections.forEach {
            val recipeSet = getRecipeSet(it.id)
            if (recipeSet.recipes.containsKey(recipeId.toString())) {
                deleteRecipeFromSet(recipeSet.id, recipeId)
                return@forEach
            }
        }
    }

    private suspend fun deleteRecipeFromSet(recipeSetId: String, recipeId: Int): Void =
        suspendCoroutine {
            recipeSetReference.child(recipeSetId).child("recipes").child(recipeId.toString())
                .removeValue()
                .addOnSuccessListener { result ->
                    it.resume(result)
                }
        }

    private suspend fun getRecipeSet(id: String): RecipeSet = suspendCoroutine {
        recipeSetReference.child(id).get()
            .addOnSuccessListener { data ->
                data.getValue(RecipeSet::class.java)?.apply {
                    it.resume(this)
                }
            }
    }

    private suspend fun deleteRecipe(recipeId: Int): Void = suspendCoroutine {
        recipesReference.child(recipeId.toString()).removeValue()
            .addOnSuccessListener { result ->
                it.resume(result)
            }
    }


    private suspend fun addRecipeToCollection(recipeSetId: String, recipeId: Int): Void =
        suspendCoroutine {
            recipeSetReference.child(recipeSetId).child("recipes").child(recipeId.toString())
                .setValue(true)
                .addOnSuccessListener { result ->
                    it.resume(result)
                }
        }

    private suspend fun saveRecipe(recipe: RecipeDetails): Recipe = suspendCoroutine {
        val dbRecipe = detailedToRecipeMapper.map(recipe)
        recipesReference.child(dbRecipe.apiId.toString()).setValue(dbRecipe)
            .addOnSuccessListener { result ->
                it.resume(dbRecipe)
            }
    }

    private suspend fun getUserCollections(recipeSetIdList: List<String>): MutableList<RecipeCollection> {
        val collections = mutableListOf<RecipeCollection>()
        recipeSetIdList.forEach { recipeSetId ->
            collections.add(
                getRecipeSetById(recipeSetId)
            )
        }
        return collections
    }

    private suspend fun getRecipeSetById(recipeSetId: String): RecipeCollection = suspendCoroutine {
        recipeSetReference.child(recipeSetId).get()
            .addOnSuccessListener { data ->
                data.getValue(RecipeSet::class.java)?.apply {
                    it.resume(
                        recipeSetToCollectionMapper.map(this)
                    )
                }
            }
    }

    private suspend fun getRecipes(recipeIdList: List<Int>): MutableList<RecipeSimple> {
        val recipeList = mutableListOf<RecipeSimple>()
        recipeIdList.forEach { recipeId ->
            recipeList.add(getRecipeById(recipeId))
        }
        return recipeList
    }

    private suspend fun getRecipeById(recipeId: Int): RecipeSimple = suspendCoroutine {
        recipesReference.child(recipeId.toString()).get()
            .addOnSuccessListener { recipeData ->
                recipeData.getValue(Recipe::class.java)?.apply {
                    it.resume(
                        recipeToSimpleRecipeMapper.map(this)
                    )
                }
            }
    }

    private suspend fun getRecipeIdList(recipeSetId: String): List<Int> = suspendCoroutine {
        val recipeIdList = mutableListOf<Int>()
        recipeSetReference.child(recipeSetId).get()
            .addOnSuccessListener { recipeSetId ->
                recipeSetId.getValue(RecipeSet::class.java)?.apply {
                    this.recipes.keys.forEach { recipeId ->
                        recipeIdList.add(recipeId.toInt())
                    }
                    it.resume(recipeIdList)
                }
            }
    }

    private suspend fun getCollectionIds(): List<String> = suspendCoroutine {
        val firebaseUser = getCurrentUser()
        val list = mutableListOf<String>()
        usersReference.child(firebaseUser.uid).get()
            .addOnSuccessListener { data ->
                data.getValue(User::class.java)?.apply {
                    recipeSets?.keys?.forEach { id ->
                        list.add(id)
                    }
                    it.resume(list)
                }
            }
    }

    private suspend fun removeRecipeSetFromUser(recipeSetId: String): Void = suspendCoroutine {
        val firebaseUser = getCurrentUser()
        usersReference.child(firebaseUser.uid).child("recipeSets")
            .child(recipeSetId).removeValue()
            .addOnSuccessListener { result ->
                it.resume(result)
            }
    }

    private suspend fun removeRecipeSet(recipeSetId: String): Void = suspendCoroutine {
        recipeSetReference.child(recipeSetId).removeValue()
            .addOnSuccessListener { result ->
                it.resume(result)
            }
    }

    private suspend fun addCollection(name: String): RecipeSet {
        val set = createCollection(name)
        saveRecipeSet(set)
        val user = saveRecipeSetToUser(set)
        updateUser(user)
        return set
    }

    private suspend fun saveRecipeSetToUser(set: RecipeSet): User = suspendCoroutine {
        val firebaseUser = getCurrentUser()
        usersReference.child(firebaseUser.uid).get()
            .addOnSuccessListener { data ->
                data.getValue(User::class.java)?.apply {
                    val user = this
                    user.recipeSets?.put(set.id, true)
                    it.resume(user)
                }
            }
    }

    private suspend fun updateUser(user: User): Boolean = suspendCoroutine {
        val firebaseUser = getCurrentUser()
        usersReference.child(firebaseUser.uid).updateChildren(user.toMap())
        it.resume(true)
    }

    private fun saveRecipeSet(set: RecipeSet) {
        recipeSetReference.child(set.id).setValue(set)
    }

    private fun createCollection(name: String): RecipeSet =
        RecipeSet(UUID.randomUUID().toString(), name, HashMap())

    private fun getCurrentUser() = auth.currentUser ?: throw UserNotFoundException("User not found")
}
