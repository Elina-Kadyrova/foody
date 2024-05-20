package com.itis.foody.features.recipe.data.response.popularRecipes


import com.google.gson.annotations.SerializedName

data class Temperature(
    @SerializedName("number")
    val number: Double?,
    @SerializedName("unit")
    val unit: String?
)
