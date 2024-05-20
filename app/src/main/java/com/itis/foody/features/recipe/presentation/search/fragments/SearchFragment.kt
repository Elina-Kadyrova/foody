package com.itis.foody.features.recipe.presentation.search.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itis.foody.R
import com.itis.foody.common.extensions.hideDataLoading
import com.itis.foody.common.extensions.hideLoading
import com.itis.foody.common.extensions.showDataLoading
import com.itis.foody.common.extensions.showLoading
import com.itis.foody.common.utils.ResourceManager
import com.itis.foody.databinding.FragmentSearchBinding
import com.itis.foody.features.recipe.domain.models.RecipeSimple
import com.itis.foody.features.recipe.domain.tempData.RecipeTagRepository
import com.itis.foody.features.recipe.presentation.rv.popularRecipes.PopularRecipesAdapter
import com.itis.foody.features.recipe.presentation.rv.recipes.RecipeAdapter
import com.itis.foody.features.recipe.presentation.search.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject
    lateinit var resourceManager: ResourceManager

    private lateinit var binding: FragmentSearchBinding
    private lateinit var recipeSearchAdapter: RecipeAdapter
    private lateinit var popularRecipesAdapter: PopularRecipesAdapter
    private lateinit var lastSeenRecipesAdapter: RecipeAdapter

    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        showDataLoading()
        initAdapters()
        initRecyclerViews()
        initSearchView()
        initObservers()
        getLastSeen()
    }

    private fun initObservers() {
        with(viewModel) {
            recipes.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { list ->
                    updateResultList(list)
                    hideLoading()
                }, onFailure = {
                    hideLoading()
                    showErrorText()
                })
            }
            lastSeen.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { list ->
                    loadLastSeenRecipes(list)
                    hideDataLoading()
                }, onFailure = { e ->
                    Log.e("LAST_SEEN", "Failed to load last seen recipes - $e")
                })
            }
        }
    }

    private fun initRecyclerViews() {
        with(binding) {
            rvLastSeen.apply {
                adapter = lastSeenRecipesAdapter
            }
            rvPopularRecipes.apply {
                adapter = popularRecipesAdapter
            }
            rvResults.apply {
                adapter = recipeSearchAdapter
            }
        }
    }

    private fun getLastSeen() {
        viewModel.loadLastSeen()
    }

    private fun initAdapters() {
        recipeSearchAdapter = RecipeAdapter {
            showRecipe(it)
        }
        lastSeenRecipesAdapter = RecipeAdapter {
            navigateToRecipeFragment(it)
        }
        popularRecipesAdapter = PopularRecipesAdapter(RecipeTagRepository.recipeTags.shuffled()) {
            navigateToRecipeListFragment(it)
        }
    }

    private fun loadLastSeenRecipes(list: MutableList<RecipeSimple>) {
        if (list.isNotEmpty()) {
            lastSeenRecipesAdapter.submitList(list)
            if (!binding.rvResults.isVisible) {
                binding.tvLastSeen.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorText() {
        with(binding) {
            rvResults.visibility = View.GONE
            tvNoRecipeFound.visibility = View.VISIBLE
        }
    }

    private fun updateResultList(list: MutableList<RecipeSimple>?) {
        recipeSearchAdapter.submitList(list)
        showList()
    }

    private fun showList() {
        with(binding) {
            rvResults.visibility = View.VISIBLE
            groupSearch.visibility = View.GONE
        }
    }

    private fun initSearchView() {
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading()
                searchByQuery(query?.trim().toString())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    private fun searchByQuery(query: String) {
        viewModel.getRecipesByQuery(query)
    }

    private fun showRecipe(id: Int) {
        navigateToRecipeFragment(id)
    }

    private fun navigateToRecipeFragment(id: Int) {
        findNavController().navigate(
            R.id.action_searchFragment_to_detailRecipeFragment,
            bundleOf("RECIPE_ID" to id)
        )
    }

    private fun navigateToRecipeListFragment(collectionTag: String) {
        findNavController().navigate(
            R.id.action_searchFragment_to_recipeListFragment,
            bundleOf("COLLECTION_TAG" to collectionTag)
        )
    }
}
