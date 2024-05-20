package com.itis.foody.common.di.modules

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.itis.foody.common.di.modules.qualifiers.ImagesReference
import com.itis.foody.common.di.modules.qualifiers.RecipeSetsReference
import com.itis.foody.common.di.modules.qualifiers.RecipesReference
import com.itis.foody.common.di.modules.qualifiers.UsersReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @UsersReference
    fun providesUsersReference(database: DatabaseReference) = database.child("users")

    @Provides
    @RecipeSetsReference
    fun providesRecipeSetsReference(database: DatabaseReference) = database.child("recipeSets")

    @Provides
    @RecipesReference
    fun providesRecipesReference(database: DatabaseReference) = database.child("recipes")

    @Provides
    @ImagesReference
    fun providesImagesReference(database: StorageReference) = database.child("images")
}
