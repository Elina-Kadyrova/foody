package com.itis.foody.features.user.domain.service

import com.itis.foody.common.validators.EmailValidator
import com.itis.foody.common.validators.UsernameValidator
import javax.inject.Inject

class UserDataValidationService @Inject constructor(
    private val usernameValidator: UsernameValidator,
    private val emailValidator: EmailValidator
) {

    fun validateUsername(username: String) {
        usernameValidator.validate(username)
    }

    fun validateEmail(email: String) {
        emailValidator.validate(email)
    }
}
