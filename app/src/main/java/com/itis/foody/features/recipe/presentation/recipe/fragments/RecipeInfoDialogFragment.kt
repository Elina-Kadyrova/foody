package com.itis.foody.features.recipe.presentation.recipe.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itis.foody.R
import com.itis.foody.databinding.DialogFragmentRecipeInfoBinding
import com.itis.foody.features.recipe.domain.models.NutrientsInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeInfoDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentRecipeInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_fragment_recipe_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogFragmentRecipeInfoBinding.bind(view)

        setNutrientsInfo()
    }

    private fun setNutrientsInfo() {
        with(binding){
            arguments?.let {
                tvCaloriesAmount.text = it.getString("CALORIES")
                tvCarbsAmount.text = it.getString("CARBS")
                tvFatsAmount.text = it.getString("FAT")
                tvProteinAmount.text = it.getString("PROTEIN")
            }
        }
    }
}
