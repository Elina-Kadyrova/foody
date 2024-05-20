package com.itis.foody.common.db.entities

import com.google.firebase.database.Exclude

data class User(
    var username: String,
    var email: String,
    var profileImage: String?,
    var recipeSets: MutableMap<String, Any>?
) {
    constructor() : this("", "", null, HashMap())

    @Exclude
    fun toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["username"] = username
        map["email"] = email
        map["profileImage"] = profileImage
        map["recipeSets"] = recipeSets
        return map
    }
}
