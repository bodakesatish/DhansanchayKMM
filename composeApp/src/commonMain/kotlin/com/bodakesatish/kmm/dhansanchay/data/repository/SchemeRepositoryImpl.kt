package com.bodakesatish.kmm.dhansanchay.data.repository

import com.bodakesatish.kmm.dhansanchay.data.source.local.SchemeLocalDataSource
import com.bodakesatish.kmm.dhansanchay.data.source.remote.SchemeRemoteDataSource
import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel
import com.bodakesatish.kmm.dhansanchay.domain.repository.SchemeRepository
import com.bodakesatish.kmm.dhansanchay.domain.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SchemeRepositoryImpl(
    private val remoteDataSource: SchemeRemoteDataSource,
    private val localDataSource: SchemeLocalDataSource
) : SchemeRepository {

    override fun observeSchemeList(isForceRefresh: Boolean): Flow<NetworkResult<List<SchemeModel>>> = flow {
        emit(value = NetworkResult.Loading)
        val needsNetworkFetch = isForceRefresh || localDataSource.count() == 0
        if (needsNetworkFetch) {
            try {
                var response : NetworkResult<List<SchemeNetworkModel>> = remoteDataSource.fetchSchemeList()

                when (response) {
                    is NetworkResult.Success -> {
                        withContext(Dispatchers.IO) { // Perform database operations on IO dispatcher
                            localDataSource.insertBatchSchemes(response.data)
                        }
                    }
                    is NetworkResult.Error -> {
                        emit(NetworkResult.Error(message = "Network error: ${response.message}", exception = response.exception))
                    }
                    is NetworkResult.Loading -> {
                        emit(NetworkResult.Loading)
                    }
                }
            } catch (e: Exception) {
//                Log.e("MFSchemeRepository", "Network error fetching schemes", e)
                // If network fails, still try to emit cached data if any
                if (localDataSource.count() == 0) {
                    emit(NetworkResult.Error(message = "Network error: ${e.message}", exception = e))
                }
            }
        }

        // Always emit the latest from the database as a Flow
        // This Flow will automatically update when the database content changes.
        try {
            // Use SQLDelight's Flow extension
            localDataSource.selectAll().collect { domainList ->
                emit(NetworkResult.Success(domainList))
            }
        } catch (e: Exception) {
//            Log.e("MFSchemeRepository", "Database error emitting schemes", e)
            emit(NetworkResult.Error(message = "Database error: ${e.message}", exception = e))
        }

    }.flowOn(Dispatchers.IO) // Ensure most of the flow logic runs on IO


    override fun clearCache() {

    }


}