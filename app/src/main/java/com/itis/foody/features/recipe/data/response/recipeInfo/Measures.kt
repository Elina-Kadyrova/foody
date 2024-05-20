package com.itis.foody.features.recipe.data.response.recipeInfo


import com.google.gson.annotations.SerializedName

data class Measures(
    @SerializedName("metric")
    val metric: Metric?,
    @SerializedName("us")
    val us: Us?
)
