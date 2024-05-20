package com.itis.foody.common.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getInteger(id: Int): Int{
        return context.resources.getInteger(id)
    }
}
