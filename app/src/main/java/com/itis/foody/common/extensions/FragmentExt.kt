package com.itis.foody.common.extensions

import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.itis.foody.MainActivity
import com.itis.foody.R

fun Fragment.navigateBack() {
    findNavController().popBackStack()
}

fun Fragment.showMessage(message: String) {
    Snackbar.make(
        requireActivity().findViewById(R.id.container),
        message,
        Snackbar.LENGTH_LONG
    ).show()
}

fun Fragment.showDataLoading(){
    (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
    this.view?.findViewById<ConstraintLayout>(R.id.layout)?.visibility = View.GONE
}

fun Fragment.hideDataLoading(){
    (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
    this.view?.findViewById<ConstraintLayout>(R.id.layout)?.visibility = View.VISIBLE
}

fun Fragment.showRecipeLoading(){
    (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
    this.view?.findViewById<CoordinatorLayout>(R.id.layout)?.visibility = View.GONE
}

fun Fragment.hideRecipeLoading(){
    (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
    this.view?.findViewById<CoordinatorLayout>(R.id.layout)?.visibility = View.VISIBLE
}

fun Fragment.showLoading(){
    (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
}

fun Fragment.hideLoading(){
    (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
}


