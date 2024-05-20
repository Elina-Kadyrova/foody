package com.itis.foody.common.validators

import android.util.Patterns
import com.itis.foody.common.exceptions.InvalidEmailException
import javax.inject.Inject

class EmailValidator @Inject constructor() : Validator {

    override fun validate(data: String) {
        if (!(data.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(data).matches())) {
            throw InvalidEmailException("Invalid email")
        }
    }
}
