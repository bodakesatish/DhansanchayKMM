package com.bodakesatish.kmm.dhansanchay.data.source.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SchemeNetworkModel(
    val schemeCode: Int,
    val schemeName: String
)