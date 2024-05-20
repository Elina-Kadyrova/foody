package com.itis.foody.features.signin.domain.services

import com.itis.foody.common.validators.EmailValidator
import com.itis.foody.common.validators.PasswordValidator
import javax.inject.Inject

class SignInValidationService @Inject constructor(
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) {

    fun validateUserForm(email: String, password: String){
        validateEmail(email)
        validatePassword(password)
    }

    private fun validateEmail(email: String) {
        emailValidator.validate(email)
    }

    private fun validatePassword(password: String) {
        passwordValidator.validate(password)
    }
}
