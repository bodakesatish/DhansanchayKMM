package com.bodakesatish.kmm.dhansanchay.domain.repository

import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel
import com.bodakesatish.kmm.dhansanchay.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SchemeRepository {
    // Option 1: Return Flow for observable data (recommended for lists)
    fun observeSchemeList(isForceRefresh: Boolean): Flow<NetworkResult<List<SchemeNetworkModel>>>
    fun clearCache()
//
//    // Option 2: Suspend function for one-time fetch (if UI doesn't need to observe changes directly)
//    suspend fun getSchemeList(): NetworkResult<List<SchemeModel>>

}