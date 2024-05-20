package com.itis.foody.features.user.presentation.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.itis.foody.R
import com.itis.foody.common.extensions.hideDataLoading
import com.itis.foody.common.extensions.showDataLoading
import com.itis.foody.common.extensions.showMessage
import com.itis.foody.databinding.FragmentProfileBinding
import com.itis.foody.features.user.domain.models.Account
import com.itis.foody.features.user.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        showDataLoading()
        setListeners()
        initObservers()
        getUser()
    }

    private fun getUser() {
        viewModel.getSessionUser()
    }

    private fun initObservers() {
        with(viewModel) {
            user.observe(viewLifecycleOwner) {
                it.fold(onSuccess = {
                    navigateToHelloFragment()
                }, onFailure = {
                    showMessage("Problems with logout. please, try again.")
                })
            }
            sessionUser.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { user ->
                    updateUI(user)
                    setActionBarAttrs(user.username)
                }, onFailure = {
                    showMessage("Problems with loading data")
                })
            }
            profileImage.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { uri ->
                    loadImage(uri)
                }, onFailure = {
                })
            }
        }
    }

    private fun loadImage(uri: Uri) {
        binding.ivAccount.load(uri)
        hideDataLoading()
    }

    private fun updateUI(user: Account) {
        with(binding) {
            tvEmail.text = user.email
            tvUsername.text = user.username
            user.profileImage?.let {
                loadProfileImage(it)
            } ?: hideDataLoading()
        }
    }

    private fun loadProfileImage(image: String) {
        viewModel.getImageUri(image)
    }

    private fun setActionBarAttrs(username: String) {
        with(binding) {
            toolbar.title = username
        }
    }

    private fun setListeners() {
        with(binding) {
            clLogout.setOnClickListener {
                showLogoutDialog()
            }
            clAccountSettings.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_userSettingsFragment
                )
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Do you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun navigateToHelloFragment() {
        findNavController().navigate(
            R.id.action_profileFragment_to_welcomeFragment
        )
    }
}
