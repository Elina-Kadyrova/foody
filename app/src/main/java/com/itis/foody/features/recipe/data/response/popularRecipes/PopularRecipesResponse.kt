package com.itis.foody.features.recipe.data.response.popularRecipes


import com.google.gson.annotations.SerializedName

data class PopularRecipesResponse(
    @SerializedName("recipes")
    val recipes: List<Recipe>?
)
