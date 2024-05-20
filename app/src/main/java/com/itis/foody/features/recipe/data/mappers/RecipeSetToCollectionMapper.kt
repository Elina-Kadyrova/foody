package com.itis.foody.features.recipe.data.mappers

import com.itis.foody.common.db.entities.RecipeSet
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import javax.inject.Inject

class RecipeSetToCollectionMapper @Inject constructor() : ModelMapper<RecipeSet, RecipeCollection> {

    override fun map(source: RecipeSet): RecipeCollection = RecipeCollection(
        source.id,
        source.name
    )
}
