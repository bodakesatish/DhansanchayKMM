package com.bodakesatish.kmm.dhansanchay.data.repository

import com.bodakesatish.kmm.dhansanchay.data.source.remote.SchemeRemoteDataSource
import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel
import com.bodakesatish.kmm.dhansanchay.domain.repository.SchemeRepository
import com.bodakesatish.kmm.dhansanchay.domain.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SchemeRepositoryImpl(
    private val remoteDataSource: SchemeRemoteDataSource
) : SchemeRepository {

    override fun observeSchemeList(isForceRefresh: Boolean): Flow<NetworkResult<List<SchemeNetworkModel>>> = flow {
        emit(value = NetworkResult.Loading)
        val needsNetworkFetch = isForceRefresh
        val response = remoteDataSource.fetchSchemeList()
        when (response) {
            is NetworkResult.Success -> {
                emit(NetworkResult.Success(response.data))
            }
            is NetworkResult.Error -> {
                emit(NetworkResult.Error(message = "Network error: ${response.message}", exception = response.exception))
            }
            is NetworkResult.Loading -> {
                emit(NetworkResult.Loading)
            }
        }
    }

    override fun clearCache() {

    }


}