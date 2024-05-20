package com.itis.foody.features.user.domain.repositories

import android.net.Uri
import android.widget.ImageView
import com.google.firebase.auth.FirebaseUser
import com.itis.foody.features.user.domain.models.Account

interface UserRepository {
    suspend fun getUser(): Account
    suspend fun changeUserData(username: String, email: String, password: String): Account
    suspend fun logout(): FirebaseUser?
    suspend fun setProfileImage(image: ImageView): Boolean
    suspend fun getImage(fileName: String): Uri
}
