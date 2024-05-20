package com.itis.foody.common.di.modules

import com.itis.foody.common.db.entities.Recipe
import com.itis.foody.common.db.entities.RecipeSet
import com.itis.foody.common.db.entities.User
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.recipe.data.mappers.*
import com.itis.foody.features.recipe.data.response.popularRecipes.PopularRecipesResponse
import com.itis.foody.features.recipe.data.response.recipeInfo.RecipeInfoResponse
import com.itis.foody.features.recipe.data.response.searchByIngredient.RecipeListByIngredientResponse
import com.itis.foody.features.recipe.data.response.searchByName.RecipeListByNameResponse
import com.itis.foody.features.recipe.data.response.similarRecipes.SimilarRecipesResponse
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.user.data.mappers.UserDataMapper
import com.itis.foody.features.user.domain.models.Account
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MapperModule {

    @Binds
    fun bindsSimilarRecipesMapper(
        impl: SimilarRecipeListMapper
    ): ModelMapper<SimilarRecipesResponse, MutableList<RecipeSimple>>

    @Binds
    fun bindsRecipeDetailsMapper(
        impl: RecipeDetailsMapper
    ): ModelMapper<RecipeInfoResponse, RecipeDetails>

    @Binds
    fun bindsPopularRecipesMapper(
        impl: PopularRecipeListMapper
    ): ModelMapper<PopularRecipesResponse, MutableList<RecipeSimple>>

    @Binds
    fun bindsRecipesByNameMapper(
        impl: ListByNameMapper
    ): ModelMapper<RecipeListByNameResponse, MutableList<RecipeSimple>>

    @Binds
    fun bindsRecipesByIngredientMapper(
        impl: ListByIngredientMapper
    ): ModelMapper<RecipeListByIngredientResponse, MutableList<RecipeSimple>>

    @Binds
    fun bindsUserDataMapper(
        impl: UserDataMapper
    ): ModelMapper<User, Account>

    @Binds
    fun bindsRecipeToSimpleRecipeMapper(
        impl: RecipeSimpleByRecipeMapper
    ): ModelMapper<Recipe, RecipeSimple>

    @Binds
    fun bindsRecipeDetailsToRecipeMapper(
        impl: RecipeDetailsToRecipeMapper
    ): ModelMapper<RecipeDetails, Recipe>

    @Binds
    fun bindsRecipeDetailsToSimpleMapper(
        impl: RecipeDetailsToRecipeSimpleMapper
    ): ModelMapper<RecipeDetails, RecipeSimple>

    @Binds
    fun bindsRecipeSetToCollection(
        impl: RecipeSetToCollectionMapper
    ): ModelMapper<RecipeSet, RecipeCollection>
}
