package com.itis.foody.common.di.modules

import com.itis.foody.common.db.converters.ImageToBytesConverter
import com.itis.foody.features.recipe.domain.utils.RecipeDataConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ConverterModule {

    @Provides
    fun providesRecipeDataConverter() = RecipeDataConverter()

    @Provides
    fun providesImageToBytesConverter() = ImageToBytesConverter()
}
