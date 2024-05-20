package com.itis.foody.features.user.domain.usecases

import com.itis.foody.features.user.domain.models.Account
import com.itis.foody.features.user.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(username: String, email: String, password: String): Account {
        return withContext(dispatcher) {
            userRepository.changeUserData(username, email, password)
        }
    }
}
