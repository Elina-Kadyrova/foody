package com.itis.foody.common.db.searchStory

import com.itis.foody.features.recipe.domain.models.RecipeSimple

object LastSeenRepository {
    private val list = mutableListOf<RecipeSimple>()

    fun getList(): MutableList<RecipeSimple> = list

    fun addNew(recipeSimple: RecipeSimple) {
        if(list.contains(recipeSimple)){
            list.remove(recipeSimple)
        }
        list.add(0, recipeSimple)
    }
}
