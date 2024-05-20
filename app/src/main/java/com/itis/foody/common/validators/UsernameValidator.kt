package com.itis.foody.common.validators

import com.itis.foody.common.exceptions.InvalidUsernameException
import com.itis.foody.common.exceptions.TooShortUsernameException
import javax.inject.Inject

private const val USERNAME_MIN_LENGTH = 6

class UsernameValidator @Inject constructor() : Validator {

    override fun validate(data: String) {
        if (data.isBlank()) {
            throw InvalidUsernameException("Username can't be empty")
        }
        if (data.length < USERNAME_MIN_LENGTH) {
            throw TooShortUsernameException("Username is too short")
        }
    }
}
