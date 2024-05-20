package com.itis.foody.common.db.entities

data class RecipeSet(
    var id: String,
    var name: String,
    var recipes: MutableMap<String, Any>
){
    constructor() : this("", "", HashMap())
}
