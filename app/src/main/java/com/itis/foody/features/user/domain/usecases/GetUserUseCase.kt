package com.itis.foody.features.user.domain.usecases

import com.itis.foody.features.user.domain.models.Account
import com.itis.foody.features.user.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): Account {
        return withContext(dispatcher) {
            userRepository.getUser()
        }
    }
}
