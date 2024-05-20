package com.itis.foody.features.signin.domain.repositories

import com.google.firebase.auth.FirebaseUser
import com.itis.foody.features.signin.domain.models.UserForm

interface SignInRepository {
    suspend fun auth(user: UserForm): FirebaseUser
}
