package com.itis.foody.features.recipe.domain.models

data class RecipeDetails(
    val id: Int,
    val image: String,
    val title: String,
    val summary: String,
    val ingredients: List<Ingredient>,
    val recipeSteps: List<RecipeStep>,
    val nutrientsInfo: NutrientsInfo,
    val readyInMinutes: Int,
    val servings: Int
)
