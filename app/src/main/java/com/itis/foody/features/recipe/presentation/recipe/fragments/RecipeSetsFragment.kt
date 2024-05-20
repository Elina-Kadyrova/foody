package com.itis.foody.features.recipe.presentation.recipe.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.extensions.*
import com.itis.foody.databinding.FragmentRecipeSetsBinding
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.presentation.recipe.viewModels.RecipeSetViewModel
import com.itis.foody.features.recipe.presentation.rv.recipeSets.RecipeSetAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeSetsFragment : Fragment(R.layout.fragment_recipe_sets) {

    private lateinit var binding: FragmentRecipeSetsBinding
    private lateinit var recipeSetAdapter: RecipeSetAdapter

    private val viewModel: RecipeSetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecipeSetsBinding.bind(view)
        recipeSetAdapter = RecipeSetAdapter { id, name -> showSavedRecipes(id, name) }

        showDataLoading()
        initRecyclerView()
        initObservers()
        getCollections()
        initListeners()
    }

    private fun getCollections() {
        viewModel.getCollections()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            showDialog()
        }
    }

    private fun initObservers() {
        with(viewModel) {
            userCollection.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { list ->
                    updateList(list)
                    hideDataLoading()
                }, onFailure = { e ->
                    Log.e("RECIPE_COLLECTION", "Failed to load - $e")
                })
            }
            newCollection.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { list ->
                    updateList(list)
                    hideLoading()
                    showMessage("Successfully created")
                }, onFailure = { e ->
                    Log.e("CREATE_COLLECTION", "Failed to create collection - $e")
                })
            }
        }
    }

    private fun updateList(list: MutableList<RecipeCollection>) {
        recipeSetAdapter.submitList(list)
        setVisibility(list)
    }

    private fun showDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_create_collection, null)
        val newCollection = view.findViewById<EditText>(R.id.et_new_collection)

        AlertDialog.Builder(requireContext())
            .setView(view)
            .setMessage("Create new collection:")
            .setPositiveButton("Save") { _, _ ->
                if (newCollection.text.toString().isNotEmpty()) {
                    createNewSet(newCollection.text.toString())
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun createNewSet(name: String) {
        viewModel.createNewSet(name)
        showLoading()
    }

    private fun setVisibility(list: MutableList<RecipeCollection>) {
        if (list.size > 0) showRecyclerView()
        else hideRecyclerView()
    }

    private fun hideRecyclerView() {
        with(binding) {
            rvRecipeSets.visibility = View.GONE
            tvNoRecipesSaved.visibility = View.VISIBLE
        }
    }

    private fun showRecyclerView() {
        with(binding) {
            rvRecipeSets.visibility = View.VISIBLE
            tvNoRecipesSaved.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        binding.rvRecipeSets.apply {
            adapter = recipeSetAdapter
        }
    }

    private fun showSavedRecipes(id: String, name: String) {
        findNavController().navigate(
            R.id.action_recipeSetsFragment_to_savedRecipesFragment,
            bundleOf(
                "COLLECTION_ID" to id,
                "COLLECTION_NAME" to name
            )
        )
    }
}
