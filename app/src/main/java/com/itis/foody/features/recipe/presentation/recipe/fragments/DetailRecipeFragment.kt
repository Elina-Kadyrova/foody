package com.itis.foody.features.recipe.presentation.recipe.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.foody.R
import com.itis.foody.common.extensions.*
import com.itis.foody.databinding.FragmentDetailRecipeBinding
import com.itis.foody.features.recipe.domain.models.Ingredient
import com.itis.foody.features.recipe.domain.models.RecipeCollection
import com.itis.foody.features.recipe.domain.models.RecipeDetails
import com.itis.foody.features.recipe.domain.models.RecipeStep
import com.itis.foody.features.recipe.domain.utils.RecipeDataConverter
import com.itis.foody.features.recipe.presentation.recipe.viewModels.DetailedRecipeViewModel
import com.itis.foody.features.recipe.presentation.rv.ingredients.IngredientAdapter
import com.itis.foody.features.recipe.presentation.rv.itemDecorators.SpaceItemDecorator
import com.itis.foody.features.recipe.presentation.rv.recipeSteps.RecipeStepAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailRecipeFragment : Fragment(R.layout.fragment_detail_recipe) {

    private lateinit var binding: FragmentDetailRecipeBinding
    private lateinit var recipe: RecipeDetails

    private var isRecipeAlreadySaved: Boolean? = null

    private lateinit var ingredientsAdapter: IngredientAdapter
    private lateinit var recipeStepAdapter: RecipeStepAdapter

    private val viewModel: DetailedRecipeViewModel by viewModels()

    @Inject
    lateinit var dataConverter: RecipeDataConverter

    private val recipeId: Int by lazy {
        arguments?.getInt("RECIPE_ID") ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailRecipeBinding.bind(view)

        showRecipeLoading()
        initObservers()
        getRecipeInfo()
        setActionBarAttrs()
        checkIfAlreadySaved()
        initListeners()
    }

    private fun checkIfAlreadySaved() {
        viewModel.checkIfRecipeAlreadySaved(recipeId)
    }

    private fun initObservers() {
        with(viewModel) {
            recipeInfo.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = { recipeInfo ->
                    recipe = recipeInfo
                    updateUI()
                    setIngredients(recipeInfo.ingredients)
                    setRecipeSteps(recipeInfo.recipeSteps)
                    updateLastSeen()
                    hideRecipeLoading()
                }, onFailure = { e ->
                    Log.e("ERROR", e.toString())
                    showMessage("Recipe not available")
                    navigateBack()
                })
            }
            savedRecipe.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = {
                    updateIcon(true)
                    isRecipeAlreadySaved = true
                    hideLoading()
                    showMessage("Successfully saved")
                }, onFailure = {
                })
            }
            removedRecipe.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = {
                    isRecipeAlreadySaved = false
                    updateIcon(false)
                    hideLoading()
                    showMessage("Remove recipe from saved")
                }, onFailure = {
                })
            }
            isSaved.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { isSaved ->
                    isRecipeAlreadySaved = isSaved
                    updateIcon(isSaved)
                }, onFailure = {
                })
            }
            userCollections.observe(viewLifecycleOwner) {
                it.fold(onSuccess = { list ->
                    processCollections(list)
                    hideLoading()
                }, onFailure = {
                })
            }
            newCollection.observe(viewLifecycleOwner) {
                it?.fold(onSuccess = { collection ->
                    saveRecipeInNewCollection(collection.id)
                }, onFailure = {
                    showMessage("Collection with the same name already exists")
                })
            }
        }
    }

    private fun processCollections(list: MutableList<RecipeCollection>) {
        if (list.size > 0) showSaveRecipeDialog(list)
        else showCreateSetDialog()
    }

    private fun updateLastSeen() {
        viewModel.updateLastSeen(recipe)
    }

    private fun showCreateSetDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_create_collection, null)
        val newCollection = view.findViewById<EditText>(R.id.et_new_collection)

        AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                if (newCollection.text.toString().isNotEmpty()) {
                    viewModel.createCollection(newCollection.text.toString())
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showSaveRecipeDialog(list: List<RecipeCollection>) {
        val view = layoutInflater.inflate(R.layout.dialog_choose_collection, null)
        val spinner = view.findViewById<Spinner>(R.id.spinner_collections)
        val newCollection = view.findViewById<EditText>(R.id.et_new_collection)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list.map { it.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                validateCollectionChoice(
                    spinner.selectedItem as String,
                    list,
                    newCollection.text.toString()
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun validateCollectionChoice(
        collection: String,
        list: List<RecipeCollection>,
        newCollectionName: String
    ) {
        if (newCollectionName.isNotEmpty()) {
            viewModel.createCollection(newCollectionName)
        } else {
            list.forEach {
                if (it.name == collection) {
                    viewModel.saveRecipe(recipe, it.id)
                    return@forEach
                }
            }
        }
    }

    private fun saveRecipeInNewCollection(collectionId: String) {
        viewModel.saveRecipe(recipe, collectionId)
    }

    private fun setRecipeSteps(recipeSteps: List<RecipeStep>) {
        recipeStepAdapter = RecipeStepAdapter(recipeSteps)
        initRecipeStepsRecycler()
    }

    private fun initRecipeStepsRecycler() {
        binding.rvRecipeSteps.apply {
            adapter = recipeStepAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
            addItemDecoration(SpaceItemDecorator(context))
        }
    }

    private fun setIngredients(ingredients: List<Ingredient>) {
        ingredientsAdapter = IngredientAdapter(ingredients, dataConverter)
        setIngredientsRecyclerView()
    }

    private fun setIngredientsRecyclerView() {
        binding.rvIngredients.apply {
            adapter = ingredientsAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
            addItemDecoration(SpaceItemDecorator(context))
        }
    }

    private fun updateIcon(flag: Boolean) {
        with(binding) {
            when (flag) {
                true -> fabSave.setImageResource(R.drawable.ic_baseline_bookmark_24)
                false -> fabSave.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
            }
        }
    }

    private fun updateUI() {
        recipe.let {
            with(binding) {
                ivRecipe.load(it.image)
                clToolbar.title = it.title
                tvServings.text = it.servings.toString()
                tvTimeOfCooking.text = dataConverter.convertTimeOfCookingToString(it.readyInMinutes)
                tvSummary.text = Html.fromHtml(
                    it.summary.substringBeforeLast("%"),
                    Html.FROM_HTML_MODE_COMPACT
                )
            }
        }
    }

    private fun getRecipeInfo() {
        viewModel.getRecipeInfo(recipeId)
    }

    private fun initListeners() {
       with(binding) {
            fabSave.setOnClickListener {
                isRecipeAlreadySaved?.apply {
                    if (this) removeRecipe()
                    else saveRecipe()
                }
            }
            fabSimilar.setOnClickListener {
                showSimilarRecipes()
            }
        }
    }

    private fun removeRecipe() {
        viewModel.deleteFromSaved(recipeId)
        showLoading()
    }

    private fun saveRecipe() {
        viewModel.getUserCollections()
        showLoading()
    }

    private fun setActionBarAttrs() {
        with(binding) {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener {
                hideLoading()
                navigateBack()
            }
        }
    }

    private fun showSimilarRecipes() {
        findNavController().navigate(
            R.id.action_detailRecipeFragment_to_recipeListFragment,
            bundleOf("SIMILAR_TO_ID" to recipe.id)
        )
    }
}
