package com.itis.foody.features.user.domain.usecases

import android.net.Uri
import com.itis.foody.features.user.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetImageUriUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(fileName: String): Uri {
        return withContext(dispatcher) {
            userRepository.getImage(fileName)
        }
    }
}
