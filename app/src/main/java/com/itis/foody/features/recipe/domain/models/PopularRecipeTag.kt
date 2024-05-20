package com.itis.foody.features.recipe.domain.models

import androidx.annotation.DrawableRes

data class PopularRecipeTag(
    val id: Int,
    val tag: String,
    @DrawableRes val image: Int
)
