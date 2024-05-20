package com.itis.foody.features.recipe.domain.models

data class Ingredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val image: String,
    val unit: String
)
