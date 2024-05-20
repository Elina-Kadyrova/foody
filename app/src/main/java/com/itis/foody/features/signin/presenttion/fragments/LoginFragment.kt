package com.itis.foody.features.signin.presenttion.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.exceptions.InvalidEmailException
import com.itis.foody.common.exceptions.InvalidPasswordException
import com.itis.foody.common.extensions.hideLoading
import com.itis.foody.common.extensions.showLoading
import com.itis.foody.common.extensions.showMessage
import com.itis.foody.common.utils.ResourceManager
import com.itis.foody.databinding.FragmentLoginBinding
import com.itis.foody.features.signin.domain.models.UserForm
import com.itis.foody.features.signin.domain.services.SignInValidationService
import com.itis.foody.features.signin.presenttion.viewModels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var resourceManager: ResourceManager

    @Inject
    lateinit var signInValidationService: SignInValidationService

    private val viewModel: SignInViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        setActionBarAttrs()
        setListeners()
        setFieldsValidationRules()
        initObservers()
    }

    private fun setListeners() {
        with(binding) {
            btnSignIn.setOnClickListener {
                cleanErrors()
                checkInputData()
            }
            tvRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }
    }

    private fun checkInputData() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPass.text.toString().trim()
        try {
            signInValidationService.validateUserForm(email, password)
            authUser(UserForm(email, password))
        } catch (e: InvalidEmailException) {
            setEmailError("Invalid email")
        } catch (e: InvalidPasswordException) {
            setPasswordError("Password is too weak")
        }
    }

    private fun setPasswordError(message: String) {
        binding.tilPass.error = message
    }

    private fun setEmailError(message: String) {
        binding.tilEmail.error = message
    }

    private fun authUser(user: UserForm) {
        showLoading()
        viewModel.authUser(user)
    }

    private fun cleanErrors() {
        with(binding) {
            tilEmail.error = null
            tilPass.error = null
        }
    }

    private fun setFieldsValidationRules() {
        val passMinLen = resourceManager.getInteger(R.integer.password_min_length)
        val passMaxLen = resourceManager.getInteger(R.integer.password_max_length)

        with(binding) {
            etPass.doOnTextChanged { text, _, _, _ ->
                if (text?.length ?: 0 < passMinLen) {
                    tilPass.helperText = "Must be $passMinLen-$passMaxLen symbols length"
                } else tilPass.helperText = null
            }
        }
    }

    private fun initObservers() {
        viewModel.user.observe(viewLifecycleOwner) {
            it?.fold(onSuccess = {
                hideLoading()
                navigateToProfile()
            }, onFailure = {
                hideLoading()
                showMessage("Authentication failed.")
            })
        }
    }

    private fun navigateToProfile() {
        findNavController().navigate(
            R.id.action_loginFragment_to_profileFragment
        )
    }

    private fun setActionBarAttrs() {
        with(binding) {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
            }
        }
    }
}
