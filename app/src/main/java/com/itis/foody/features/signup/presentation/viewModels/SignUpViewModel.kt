package com.itis.foody.features.signup.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.itis.foody.features.signup.domain.models.UserForm
import com.itis.foody.features.signup.domain.usecases.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private var _user: MutableLiveData<Result<FirebaseUser>> = MutableLiveData()
    val user: LiveData<Result<FirebaseUser>> = _user

    fun registerUser(user: UserForm) {
        viewModelScope.launch {
            kotlin.runCatching {
                registerUserUseCase(user)
            }.onSuccess {
                _user.value = Result.success(it)
            }.onFailure {
                _user.value = Result.failure(it)
            }
        }
    }
}
