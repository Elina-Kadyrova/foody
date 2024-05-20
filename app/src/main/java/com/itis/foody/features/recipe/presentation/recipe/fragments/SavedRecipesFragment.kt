package com.itis.foody.features.recipe.presentation.recipe.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.extensions.hideDataLoading
import com.itis.foody.common.extensions.navigateBack
import com.itis.foody.common.extensions.showDataLoading
import com.itis.foody.common.extensions.showMessage
import com.itis.foody.databinding.FragmentSavedRecipesBinding
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.presentation.recipe.viewModels.SavedRecipesViewModel
import com.itis.foody.features.recipe.presentation.rv.recipes.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedRecipesFragment : Fragment(R.layout.fragment_saved_recipes) {

    private lateinit var binding: FragmentSavedRecipesBinding
    private lateinit var recipeAdapter: RecipeAdapter

    private val viewModel: SavedRecipesViewModel by viewModels()

    private val collectionId: String? by lazy {
        arguments?.getString("COLLECTION_ID")
    }

    private val collectionName: String? by lazy {
        arguments?.getString("COLLECTION_NAME")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedRecipesBinding.bind(view)
        recipeAdapter = RecipeAdapter { showRecipe(it) }

        showDataLoading()
        initRecyclerView()
        setActionBarAttrs()
        setListeners()
        initObservers()
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.remove_set -> {
            showRemoveDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipe_set_actions, menu)
    }

    private fun showRemoveDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Remove this recipe collection?")
            .setPositiveButton("Yes") { _, _ ->
                removeRecipeCollection()
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun removeRecipeCollection() {
        collectionId?.let { viewModel.removeCollection(it) }
    }

    private fun loadData() {
        collectionId?.let {
            viewModel.getSavedRecipes(it)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            savedRecipes.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = { list ->
                    processData(list)
                    hideDataLoading()
                }, onFailure = { e ->
                    showMessage("Problems with loading data.")
                })
            }
            removedCollection.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = {
                    navigateBack()
                    showMessage("Removed successfully")
                }, onFailure = {
                    showMessage("Failed to remove collection")
                })
            }
        }
    }

    private fun processData(list: MutableList<RecipeSimple>) {
        if (list.size > 0) recipeAdapter.submitList(list)
        else showNoRecipesText()
    }

    private fun showNoRecipesText() {
        with(binding) {
            rvRecipes.visibility = View.GONE
            tvNoRecipesSaved.visibility = View.VISIBLE
        }
    }

    private fun initRecyclerView() {
        binding.rvRecipes.apply {
            adapter = recipeAdapter
        }
    }

    private fun setListeners() {
        with(binding) {
            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_savedRecipesFragment_to_searchFragment)
            }
        }
    }

    private fun showRecipe(id: Int) {
        findNavController().navigate(
            R.id.action_savedRecipesFragment_to_detailRecipeFragment,
            bundleOf("RECIPE_ID" to id)
        )
    }

    private fun setActionBarAttrs() {
        with(binding) {
            toolbar.title = collectionName
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
