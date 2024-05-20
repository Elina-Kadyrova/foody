package com.itis.foody.common.di.modules

import com.itis.foody.features.recipe.data.repositories.RecipeRepositoryImpl
import com.itis.foody.features.recipe.data.repositories.RecipeSearchRepositoryImpl
import com.itis.foody.features.recipe.domain.repositories.RecipeRepository
import com.itis.foody.features.recipe.domain.repositories.RecipeSearchRepository
import com.itis.foody.features.signin.data.repositories.SignInRepositoryImpl
import com.itis.foody.features.signin.domain.repositories.SignInRepository
import com.itis.foody.features.signup.data.repositories.SignUpRepositoryImpl
import com.itis.foody.features.signup.domain.repositories.SignUpRepository
import com.itis.foody.features.user.data.repositories.UserRepositoryImpl
import com.itis.foody.features.user.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Binds
    fun bindsSignUpRepository(
        impl: SignUpRepositoryImpl
    ): SignUpRepository

    @Binds
    fun bindsSignInRepository(
        impl: SignInRepositoryImpl
    ): SignInRepository

    @Binds
    fun bindsUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    fun bindsRecipeSearchRepository(
        impl: RecipeSearchRepositoryImpl
    ): RecipeSearchRepository

    @Binds
    fun bindsRecipeRepository(
        impl: RecipeRepositoryImpl
    ): RecipeRepository
}
