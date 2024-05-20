package com.itis.foody.features.recipe.data.response.searchByIngredient


import com.google.gson.annotations.SerializedName

data class RecipeListByIngredientResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("imageType")
    val imageType: String?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("missedIngredientCount")
    val missedIngredientCount: Int?,
    @SerializedName("missedIngredients")
    val missedIngredients: List<MissedIngredient>?,
    @SerializedName("title")
    val title: String,
    @SerializedName("unusedIngredients")
    val unusedIngredients: List<Any>?,
    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int?,
    @SerializedName("usedIngredients")
    val usedIngredients: List<UsedIngredient>?
)
