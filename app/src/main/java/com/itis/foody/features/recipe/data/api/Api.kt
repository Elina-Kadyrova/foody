package com.itis.foody.features.recipe.data.api

import com.itis.foody.features.recipe.data.response.popularRecipes.PopularRecipesResponse
import com.itis.foody.features.recipe.data.response.recipeInfo.RecipeInfoResponse
import com.itis.foody.features.recipe.data.response.searchByIngredient.RecipeListByIngredientResponse
import com.itis.foody.features.recipe.data.response.searchByName.RecipeListByNameResponse
import com.itis.foody.features.recipe.data.response.similarRecipes.SimilarRecipesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("complexSearch")
    suspend fun getRecipesByName(@Query("query") name: String): RecipeListByNameResponse

    @GET("findByIngredients")
    suspend fun getRecipesByIngredients(@Query("ingredients") ingredients: String): RecipeListByIngredientResponse

    @GET("{id}/information?includeNutrition=true")
    suspend fun getRecipeInfo(@Path("id") id: Int): RecipeInfoResponse

    @GET("random?number=10")
    suspend fun getPopularRecipesByTag(@Query("tags") tags: String): PopularRecipesResponse

    @GET("{id}/similar")
    suspend fun getSimilarRecipes(@Path("id") id: Int): SimilarRecipesResponse
}
