package com.itis.foody.features.signup.domain.services

import com.itis.foody.common.validators.EmailValidator
import com.itis.foody.common.validators.PasswordValidator
import com.itis.foody.common.validators.UsernameValidator
import com.itis.foody.features.signup.domain.exceptions.DifferentPasswordsException
import javax.inject.Inject

class SignUpValidationService @Inject constructor(
    private val usernameValidator: UsernameValidator,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) {

    fun validateUserForm(username: String, email: String, password: String, repPassword: String) {
        validateUsername(username)
        validateEmail(email)
        validatePassword(password)
        checkIfPasswordsAreEquals(password, repPassword)
    }

    private fun validateUsername(username: String) {
        usernameValidator.validate(username)
    }

    private fun validateEmail(email: String) {
        emailValidator.validate(email)
    }

    private fun validatePassword(password: String) {
        passwordValidator.validate(password)
    }

    private fun checkIfPasswordsAreEquals(password: String, repPassword: String) {
        if (password != repPassword) {
            throw DifferentPasswordsException("Passwords are different")
        }
    }
}
