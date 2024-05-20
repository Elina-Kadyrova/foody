package com.itis.foody.features.recipe.data.response.recipeInfo


import com.google.gson.annotations.SerializedName

data class Flavonoid(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("unit")
    val unit: String?
)
