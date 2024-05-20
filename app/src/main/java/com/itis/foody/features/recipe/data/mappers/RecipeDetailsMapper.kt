package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.features.recipe.data.response.recipeInfo.AnalyzedInstruction
import com.itis.foody.features.recipe.data.response.recipeInfo.ExtendedIngredient
import com.itis.foody.features.recipe.data.response.recipeInfo.NutrientX
import com.itis.foody.features.recipe.data.response.recipeInfo.RecipeInfoResponse
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.Ingredient
import com.itis.foody.features.recipe.domain.models.NutrientsInfo
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeStep
import javax.inject.Inject

private const val CALORIES_INDEX = 0
private const val CARBS_INDEX = 3
private const val FAT_INDEX = 1
private const val PROTEIN_INDEX = 8

class RecipeDetailsMapper @Inject constructor() : ModelMapper<RecipeInfoResponse, RecipeDetails> {

    override fun map(source: RecipeInfoResponse): RecipeDetails =
        RecipeDetails(
            source.id,
            source.image,
            source.title,
            source.summary,
            getRecipeIngredients(source.extendedIngredients),
            getRecipeSteps(source.analyzedInstructions),
            getNutrientsInfo(source.nutrition.nutrients),
            source.readyInMinutes,
            source.servings
        )

    private fun getNutrientsInfo(nutrients: List<NutrientX>): NutrientsInfo =
        NutrientsInfo(
            nutrients[CALORIES_INDEX].amount.toInt().toString(),
            nutrients[CARBS_INDEX].amount.toInt().toString(),
            nutrients[FAT_INDEX].amount.toInt().toString(),
            nutrients[PROTEIN_INDEX].amount.toInt().toString()
        )

    private fun getRecipeSteps(instructions: List<AnalyzedInstruction>): List<RecipeStep> {
        val list = mutableListOf<RecipeStep>()
        instructions[0].steps.forEach {
            list.add(
                RecipeStep(it.number, it.step)
            )
        }
        return list
    }

    private fun getRecipeIngredients(ingredients: List<ExtendedIngredient>): List<Ingredient> {
        val list = mutableListOf<Ingredient>()
        ingredients.forEach {
            list.add(
                Ingredient(it.id, it.originalName, it.amount, it.image, it.unit)
            )
        }
        return list
    }
}
