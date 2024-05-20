package com.itis.foody.features.user.domain.usecases

import android.widget.ImageView
import com.itis.foody.features.user.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(image: ImageView): Boolean {
        return withContext(dispatcher) {
            userRepository.setProfileImage(image)
        }
    }
}
