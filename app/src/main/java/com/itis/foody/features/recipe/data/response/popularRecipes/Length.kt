package com.itis.foody.features.recipe.data.response.popularRecipes


import com.google.gson.annotations.SerializedName

data class Length(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("unit")
    val unit: String?
)
