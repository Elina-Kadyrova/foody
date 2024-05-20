package com.itis.foody.features.recipe.data.response.recipeInfo


import com.google.gson.annotations.SerializedName

data class Us(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("unitLong")
    val unitLong: String?,
    @SerializedName("unitShort")
    val unitShort: String?
)
