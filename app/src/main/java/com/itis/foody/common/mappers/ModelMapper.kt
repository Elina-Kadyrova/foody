package com.itis.foody.common.mappers

interface ModelMapper<S, D> {
    fun map(source: S): D
}
