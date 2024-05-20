package com.itis.foody.features.signup.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.itis.foody.common.di.modules.qualifiers.UsersReference
import com.itis.foody.common.db.entities.User
import com.itis.foody.features.signup.domain.exceptions.RegistrationFailedException
import com.itis.foody.features.signup.domain.exceptions.SuchEmailAlreadyRegisteredException
import com.itis.foody.features.signup.domain.models.UserForm
import com.itis.foody.features.signup.domain.repositories.SignUpRepository
import java.util.HashMap
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SignUpRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @UsersReference private val reference: DatabaseReference
) : SignUpRepository {

    override suspend fun registerUser(user: UserForm): FirebaseUser = saveToCloud(user)


    private suspend fun saveToCloud(user: UserForm): FirebaseUser = suspendCoroutine {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        saveToDatabase(user, firebaseUser)
                        Log.d("SIGN UP", "Sign Up successfully")
                        it.resume(firebaseUser)
                    } else {
                        it.resumeWithException(SuchEmailAlreadyRegisteredException("Such email already registered"))
                    }
                } else {
                    Log.e("SIGN UP", "Sign up failed")
                    it.resumeWithException(RegistrationFailedException("Registration failed"))
                }
            }
    }

    private fun saveToDatabase(user: UserForm, firebaseUser: FirebaseUser) {
        reference.child(firebaseUser.uid).setValue(
            User(
                user.username,
                user.email,
                null,
                HashMap()
            )
        ).addOnSuccessListener { _ ->
            Log.d("SIGN UP", "Save successfully")
        }.addOnFailureListener {
            Log.d("SIGN UP", "Failed to save")
        }
    }

}
