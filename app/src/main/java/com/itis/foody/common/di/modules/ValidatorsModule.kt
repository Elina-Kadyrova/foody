package com.itis.foody.common.di.modules

import com.itis.foody.common.validators.EmailValidator
import com.itis.foody.common.validators.PasswordValidator
import com.itis.foody.common.validators.UsernameValidator
import com.itis.foody.common.validators.Validator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
interface ValidatorsModule {

    @Binds
    fun bindsEmailValidator(
        impl: EmailValidator
    ): Validator

    @Binds
    fun bindsUsernameValidator(
        impl: UsernameValidator
    ): Validator

    @Binds
    fun bindsPasswordValidator(
        impl: PasswordValidator
    ): Validator
}
