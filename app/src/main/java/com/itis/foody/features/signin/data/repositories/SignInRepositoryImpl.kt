package com.itis.foody.features.signin.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.itis.foody.common.di.modules.qualifiers.UsersReference
import com.itis.foody.features.signin.domain.exceptions.FirebaseAuthFailedException
import com.itis.foody.features.signin.domain.exceptions.UnknownEmailException
import com.itis.foody.features.signin.domain.exceptions.UserNotFoundException
import com.itis.foody.features.signin.domain.models.UserForm
import com.itis.foody.features.signin.domain.repositories.SignInRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SignInRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @UsersReference private val reference: DatabaseReference,
) : SignInRepository {

    override suspend fun auth(user: UserForm): FirebaseUser {
        val firebaseUser = getFirebaseUser(user.email, user.password)
        return getUserByUid(firebaseUser)
    }

    private suspend fun getFirebaseUser(email: String, password: String): FirebaseUser =
        suspendCoroutine {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null) {
                            it.resume(firebaseUser)
                        } else it.resumeWithException(
                            FirebaseAuthFailedException("Auth Failed")
                        )
                    } else it.resumeWithException(UnknownEmailException("Unknown email"))
                }
        }


    private suspend fun getUserByUid(firebaseUser: FirebaseUser): FirebaseUser = suspendCoroutine {
        reference.child(firebaseUser.uid).get()
            .addOnSuccessListener { _ ->
                it.resume(firebaseUser)
            }.addOnFailureListener { e ->
                Log.e("SIGN IN", e.toString())
                it.resumeWithException(
                    UserNotFoundException("User not found")
                )
            }
    }
}
