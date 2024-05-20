package com.itis.foody.features.recipe.data.response.searchByName


import com.google.gson.annotations.SerializedName

data class RecipeListByNameResponse(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("results")
    val results: List<Result>?,
    @SerializedName("totalResults")
    val totalResults: Int?
)
