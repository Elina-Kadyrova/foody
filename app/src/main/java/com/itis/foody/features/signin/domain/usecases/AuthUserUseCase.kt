package com.itis.foody.features.signin.domain.usecases

import com.google.firebase.auth.FirebaseUser
import com.itis.foody.features.signin.domain.models.Account
import com.itis.foody.features.signin.domain.models.UserForm
import com.itis.foody.features.signin.domain.repositories.SignInRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthUserUseCase @Inject constructor(
    private val signInRepository: SignInRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(user: UserForm): FirebaseUser {
        return withContext(dispatcher) {
            signInRepository.auth(user)
        }
    }
}
