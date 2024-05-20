package com.itis.foody.features.user.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.extensions.hideDataLoading
import com.itis.foody.common.extensions.showDataLoading
import com.itis.foody.databinding.FragmentWelcomeBinding
import com.itis.foody.features.user.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding

    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

        initObservers()
        setListeners()
        showDataLoading()
        checkIfAlreadyAuth()
    }

    private fun initObservers() {
        viewModel.sessionUser.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                navigateToProfile()
                hideDataLoading()
            }, onFailure = {
                hideDataLoading()
            })
        }
    }

    private fun navigateToProfile() {
        findNavController().navigate(
            R.id.action_global_nav_app_content
        )
    }

    private fun setListeners() {
        with(binding) {
            btnSignIn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_welcomeFragment_to_loginFragment
                )
            }
            btnSignUp.setOnClickListener {
                findNavController().navigate(
                    R.id.action_welcomeFragment_to_registrationFragment
                )
            }
        }
    }

    private fun checkIfAlreadyAuth() {
        viewModel.getSessionUser()
    }
}
