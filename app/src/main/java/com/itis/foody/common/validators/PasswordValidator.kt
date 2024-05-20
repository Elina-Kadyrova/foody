package com.itis.foody.common.validators

import com.itis.foody.common.exceptions.InvalidPasswordException
import java.util.regex.Pattern
import javax.inject.Inject

private const val PASSWORD_REGEX =
    "^.{8,20}$"

class PasswordValidator @Inject constructor() : Validator {

    override fun validate(data: String) {
        if (!Pattern.compile(PASSWORD_REGEX).matcher(data).matches()) {
            throw InvalidPasswordException("Password is too weak")
        }
    }
}
