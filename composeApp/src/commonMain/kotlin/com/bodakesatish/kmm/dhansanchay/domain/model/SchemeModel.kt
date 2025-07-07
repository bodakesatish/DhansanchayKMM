package com.bodakesatish.kmm.dhansanchay.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SchemeModel(
    val schemeCode: Long,
    val schemeName: String
)