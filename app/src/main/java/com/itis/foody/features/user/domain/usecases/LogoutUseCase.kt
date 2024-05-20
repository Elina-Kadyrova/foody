package com.itis.foody.features.user.domain.usecases

import com.google.firebase.auth.FirebaseUser
import com.itis.foody.features.user.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): FirebaseUser? {
        return withContext(dispatcher) {
            userRepository.logout()
        }
    }
}
