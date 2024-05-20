package com.itis.foody.features.recipe.data.response.similarRecipes

import com.google.gson.annotations.SerializedName

data class SimilarRecipesResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageType")
    val imageType: String?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("title")
    val title: String
)
