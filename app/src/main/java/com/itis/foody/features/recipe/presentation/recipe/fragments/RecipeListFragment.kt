package com.itis.foody.features.recipe.presentation.recipe.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.extensions.hideLoading
import com.itis.foody.common.extensions.showLoading
import com.itis.foody.common.extensions.showMessage
import com.itis.foody.databinding.FragmentRecipeListBinding
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.presentation.recipe.viewModels.RecipeListViewModel
import com.itis.foody.features.recipe.presentation.rv.recipes.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {

    private lateinit var binding: FragmentRecipeListBinding
    private lateinit var recipeAdapter: RecipeAdapter

    private val viewModel: RecipeListViewModel by viewModels()

    private val collectionTag: String? by lazy {
        arguments?.getString("COLLECTION_TAG")
    }

    private val recipeId: Int? by lazy {
        arguments?.getInt("SIMILAR_TO_ID")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipeListBinding.bind(view)
        recipeAdapter = RecipeAdapter { showRecipe(it) }

        showLoading()
        initRecyclerView()
        initObservers()
        loadContent()
    }

    private fun loadContent() {
        collectionTag?.let {
            loadPopular(it)
        } ?: recipeId?.let {
            loadSimilar(it)
        }
    }

    private fun loadPopular(tag: String) {
        viewModel.getPopularRecipes(tag)
    }

    private fun loadSimilar(id: Int) {
        viewModel.getSimilarRecipes(id)
    }

    private fun initObservers() {
        with(viewModel) {
            popularRecipes.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = { list ->
                    setData(list)
                }, onFailure = {
                    showMessage("Problems with loading data.")
                })
            }
            similarRecipes.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = { list ->
                    setData(list)
                    setToolbarText()
                }, onFailure = {
                    showMessage("Problems with loading data.")
                })
            }
        }
    }

    private fun setToolbarText() {
        binding.toolbar.title = "Similar recipes"
    }

    private fun setData(list: MutableList<RecipeSimple>) {
        recipeAdapter.submitList(list)
        hideLoading()
    }

    private fun initRecyclerView() {
        binding.rvRecipes.apply {
            adapter = recipeAdapter
        }
    }

    private fun showRecipe(id: Int) {
        /*findNavController().navigate(
            R.id.action_recipeListFragment_to_nav_detailed_recipe,
            bundleOf("RECIPE_ID" to id)
        )*/
    }
}
