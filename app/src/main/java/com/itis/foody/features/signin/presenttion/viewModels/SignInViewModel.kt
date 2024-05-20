package com.itis.foody.features.signin.presenttion.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.itis.foody.features.signin.domain.models.UserForm
import com.itis.foody.features.signin.domain.usecases.AuthUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase
) : ViewModel() {

    private var _user: MutableLiveData<Result<FirebaseUser>> = MutableLiveData()
    val user: LiveData<Result<FirebaseUser>> = _user

    fun authUser(user: UserForm) {
        viewModelScope.launch {
            runCatching {
                authUserUseCase(user)
            }.onSuccess {
                _user.value = Result.success(it)
            }.onFailure {
                _user.value = Result.failure(it)
            }
        }
    }
}
