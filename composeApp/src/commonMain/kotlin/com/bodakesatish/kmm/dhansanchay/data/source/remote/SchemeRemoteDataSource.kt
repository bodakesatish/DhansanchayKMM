package com.bodakesatish.kmm.dhansanchay.data.source.remote

import com.bodakesatish.kmm.dhansanchay.data.source.remote.api.SchemeApiService
import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import com.bodakesatish.kmm.dhansanchay.domain.utils.NetworkResult

interface SchemeRemoteDataSource {
    suspend fun fetchSchemeList(): NetworkResult<List<SchemeNetworkModel>>
}

class SchemeRemoteDataSourceImpl(
    private val remoteApiService: SchemeApiService
) : SchemeRemoteDataSource {

    override suspend fun fetchSchemeList(): NetworkResult<List<SchemeNetworkModel>> {
        val networkSchemes = remoteApiService.fetchSchemesFromServer()
        return networkSchemes
    }

}