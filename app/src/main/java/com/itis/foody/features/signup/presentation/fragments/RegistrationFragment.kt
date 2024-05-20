package com.itis.foody.features.signup.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.exceptions.InvalidEmailException
import com.itis.foody.common.exceptions.InvalidPasswordException
import com.itis.foody.common.exceptions.InvalidUsernameException
import com.itis.foody.common.exceptions.TooShortUsernameException
import com.itis.foody.common.extensions.hideLoading
import com.itis.foody.common.extensions.showLoading
import com.itis.foody.common.extensions.showMessage
import com.itis.foody.common.utils.ResourceManager
import com.itis.foody.databinding.FragmentRegistrationBinding
import com.itis.foody.features.signup.domain.exceptions.DifferentPasswordsException
import com.itis.foody.features.signup.domain.models.UserForm
import com.itis.foody.features.signup.domain.services.SignUpValidationService
import com.itis.foody.features.signup.presentation.viewModels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    @Inject
    lateinit var resourceManager: ResourceManager

    @Inject
    lateinit var signUpValidationService: SignUpValidationService

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentRegistrationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)

        setActionBarAttrs()
        setListeners()
        setFieldsValidationRules()
        initObservers()
    }

    private fun setListeners() {
        with(binding) {
            btnSignUp.setOnClickListener {
                cleanErrors()
                checkInputData()
            }
            tvLogin.setOnClickListener {
                findNavController().navigate(
                    R.id.action_registrationFragment_to_loginFragment
                )
            }
        }
    }

    private fun checkInputData() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()
        val repPassword = binding.etRepPass.text.toString().trim()
        try {
            signUpValidationService.validateUserForm(username, email, password, repPassword)
            registerUser(UserForm(username, email, password))
        } catch (e: InvalidUsernameException) {
            setUsernameError("Username can't be empty")
        } catch (e: TooShortUsernameException) {
            setUsernameError("Too short username")
        } catch (e: InvalidEmailException) {
            setEmailError("Invalid email")
        } catch (e: InvalidPasswordException) {
            setPasswordError("Password is too weak")
        } catch (e: DifferentPasswordsException) {
            setRepPasswordError("Passwords are different")
        }
    }

    private fun setRepPasswordError(message: String) {
        binding.tilRepPass.error = message
    }

    private fun setPasswordError(message: String) {
        binding.tilPass.error = message
    }

    private fun setEmailError(message: String) {
        binding.tilEmail.error = message
    }

    private fun setUsernameError(message: String) {
        binding.tilUsername.error = message
    }

    private fun registerUser(userForm: UserForm) {
        showLoading()
        viewModel.registerUser(userForm)
    }

    private fun cleanErrors() {
        with(binding) {
            tilUsername.error = null
            tilEmail.error = null
            tilPass.error = null
            tilRepPass.error = null
        }
    }

    private fun setFieldsValidationRules() {
        val passMinLen = resourceManager.getInteger(R.integer.password_min_length)
        val passMaxLen = resourceManager.getInteger(R.integer.password_max_length)
        val usernameMinLength = resourceManager.getInteger(R.integer.username_min_length)

        with(binding) {
            etPass.doOnTextChanged { text, _, _, _ ->
                if (text?.length ?: 0 < passMinLen) {
                    tilPass.helperText = "Must be $passMinLen-$passMaxLen symbols length"
                } else tilPass.helperText = null
            }
            etRepPass.doOnTextChanged { text, _, _, _ ->
                if (text?.length ?: 0 < passMinLen) {
                    tilPass.helperText = "Must be $passMinLen-$passMaxLen symbols length"
                } else tilPass.helperText = null
            }
            etUsername.doOnTextChanged { text, _, _, _ ->
                if (text?.length ?: 0 < usernameMinLength) {
                    tilUsername.helperText = "Must be $usernameMinLength-$passMaxLen symbols length"
                } else tilUsername.helperText = null
            }
        }
    }

    private fun initObservers() {
        viewModel.user.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                hideLoading()
                navigateToProfile()
            }, onFailure = {
                hideLoading()
                showMessage("Such email is already registered")
            })
        }
    }

    private fun navigateToProfile() {
        findNavController().navigate(
            R.id.action_registrationFragment_to_profileFragment
        )
    }

    private fun setActionBarAttrs() {
        with(binding) {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_welcomeFragment)
            }
        }
    }
}
