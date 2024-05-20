package com.itis.foody.common.db.entities

data class Recipe(
    var apiId: Int,
    var title: String,
    var image: String,
){
    constructor(): this(0, "", "")
}
