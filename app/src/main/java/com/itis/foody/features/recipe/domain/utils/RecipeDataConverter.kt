package com.itis.foody.features.recipe.domain.utils

import javax.inject.Inject
import kotlin.math.roundToInt

private const val INGREDIENT_IMAGE_PREFIX = "https://spoonacular.com/cdn/ingredients_100x100/"
private const val RECIPE_IMAGE_PREFIX = "https://spoonacular.com/recipeImages/"
private const val RECIPE_IMAGE_SUFFIX = "-312x231.jpg"
private const val MINUTES_IN_HOUR = 60

class RecipeDataConverter @Inject constructor() {

    fun convertIngredientImageUrl(fileName: String) = INGREDIENT_IMAGE_PREFIX + fileName

    fun convertRecipeImageUrl(id: Int) = RECIPE_IMAGE_PREFIX + id + RECIPE_IMAGE_SUFFIX

    fun convertIngredientAmountToString(amount: Double, unit: String): String =
        "${getAmount(amount)} $unit"

    fun convertTimeOfCookingToString(min: Int): String {
        var hh = ""
        if (min >= MINUTES_IN_HOUR) hh = "${(min / MINUTES_IN_HOUR)}h"
        return "$hh ${min % MINUTES_IN_HOUR}m"
    }

    private fun getAmount(amount: Double): String {
        return if ((amount % 1).equals(0.0)) amount.toInt().toString()
        else String.format("%.1f", amount)
    }
}
