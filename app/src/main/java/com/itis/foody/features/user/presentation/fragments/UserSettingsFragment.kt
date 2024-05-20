package com.itis.foody.features.user.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.itis.foody.R
import com.itis.foody.common.exceptions.InvalidEmailException
import com.itis.foody.common.exceptions.InvalidUsernameException
import com.itis.foody.common.extensions.*
import com.itis.foody.databinding.FragmentUserSettingsBinding
import com.itis.foody.features.user.domain.models.Account
import com.itis.foody.features.user.domain.service.UserDataValidationService
import com.itis.foody.features.user.presentation.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserSettingsFragment : Fragment(R.layout.fragment_user_settings) {

    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var user: Account

    private var uri: Uri? = null

    @Inject
    lateinit var userDataValidationService: UserDataValidationService

    private val viewModel: UserViewModel by viewModels()

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uri = result.data?.data
                binding.ivImage.setImageURI(uri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserSettingsBinding.bind(view)

        showDataLoading()
        initObservers()
        getUser()
        setActionBarAttrs()
        setListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save -> {
            processUserInfo()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_settings_menu, menu)
    }

    private fun setListeners() {
        binding.btnChooseImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun initObservers() {
        viewModel.sessionUser.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { user ->
                this.user = user
                updateUI(user)
            }, onFailure = {
                showMessage("Problems. Try again.")
            })
        }
        viewModel.updatedUser.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                showMessage("Data successfully updated")
                checkImage()
                hideLoading()
            }, onFailure = {
                showMessage("Problems. Please, try again.")
                hideLoading()
            })
        }
        viewModel.newImage.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                hideLoading()
                navigateBack()
            }, onFailure = {
            })
        }
        viewModel.profileImage.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { uri ->
                loadImage(uri)
                hideDataLoading()
            }, onFailure = {
            })
        }
    }

    private fun loadImage(uri: Uri) {
        binding.ivImage.load(uri)
    }

    private fun checkImage() {
        uri?.apply {
            showLoading()
            viewModel.updateProfileImage(binding.ivImage)
        } ?: navigateBack()
    }

    private fun updateUI(user: Account) {
        with(binding) {
            etUsername.setText(user.username)
            etEmail.setText(user.email)
            user.profileImage?.let {
                loadCurrentProfileImage(it)
            } ?: hideDataLoading()
        }
    }

    private fun loadCurrentProfileImage(image: String) {
        viewModel.getImageUri(image)
    }

    private fun getUser() {
        viewModel.getSessionUser()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickImageResult.launch(intent)
    }

    private fun processUserInfo() {
        val email = binding.etEmail.text.toString()
        val username = binding.etUsername.text.toString()
        if (isEmailValid(email) && isUsernameValid(username) &&
            (email != user.email || username != user.username)
        ) {
            showPasswordAlertDialog(username, email)
        } else if (uri != null) checkImage()
        else navigateBack()
    }

    private fun showPasswordAlertDialog(username: String, email: String) {
        val view = layoutInflater.inflate(R.layout.dialog_password, null)
        val text = view.findViewById<EditText>(R.id.et_pass)

        AlertDialog.Builder(requireContext())
            .setMessage("Enter your password:")
            .setView(view)
            .setPositiveButton("Send") { _, _ ->
                checkInput(username, email, text.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun checkInput(username: String, email: String, password: String) {
        if (password.isNotBlank()) try {
            viewModel.changeUserData(username, email, password)
            showLoading()
        } catch (e: Exception) {
            showMessage("Wrong password")
        }
    }

    private fun isEmailValid(email: String): Boolean =
        try {
            userDataValidationService.validateEmail(email)
            true
        } catch (e: InvalidEmailException) {
            showMessage("Invalid email")
            false
        }

    private fun isUsernameValid(username: String): Boolean =
        try {
            userDataValidationService.validateUsername(username)
            true
        } catch (e: InvalidUsernameException) {
            showMessage("Invalid username: should be 6-20 symbols length")
            false
        }

    private fun setActionBarAttrs() {
        with(binding) {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener {
                navigateBack()
            }
            toolbar.setOnMenuItemClickListener {
                onOptionsItemSelected(it)
            }
        }
    }
}
