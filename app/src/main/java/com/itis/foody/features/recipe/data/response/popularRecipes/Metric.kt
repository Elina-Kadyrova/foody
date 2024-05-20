package com.itis.foody.features.recipe.data.response.popularRecipes


import com.google.gson.annotations.SerializedName

data class Metric(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("unitLong")
    val unitLong: String?,
    @SerializedName("unitShort")
    val unitShort: String?
)
