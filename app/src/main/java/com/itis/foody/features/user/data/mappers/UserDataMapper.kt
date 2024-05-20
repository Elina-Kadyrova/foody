package com.itis.foody.features.user.data.mappers

import com.itis.foody.common.db.entities.User
import com.itis.foody.common.mappers.ModelMapper
import com.itis.foody.features.user.domain.models.Account
import javax.inject.Inject

class UserDataMapper @Inject constructor() : ModelMapper<User, Account> {

    override fun map(source: User): Account = Account(
        source.username,
        source.email,
        source.profileImage
    )
}
