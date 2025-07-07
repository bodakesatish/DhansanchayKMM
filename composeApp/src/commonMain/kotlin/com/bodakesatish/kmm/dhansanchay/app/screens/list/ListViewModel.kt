package com.bodakesatish.kmm.dhansanchay.app.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel
import com.bodakesatish.kmm.dhansanchay.domain.repository.SchemeRepository
import com.bodakesatish.kmm.dhansanchay.domain.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

data class SchemesUiState(
    val schemes: List<SchemeModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class ListViewModel(
    private val schemeRepository: SchemeRepository // Renamed for clarity (mfRepository -> schemeRepository)
    // private val savedStateHandle: SavedStateHandle // Inject if you need to save/restore specific state
) : ViewModel() {

    // Key for SavedStateHandle
    companion object {
        private const val SAVED_QUERY_KEY = "mf_query"
    }

    // Example: If you had a search query
//    private val _currentQuery = MutableStateFlow(savedStateHandle.get<String>(SAVED_QUERY_KEY) ?: "")


    private val _uiState = MutableStateFlow(SchemesUiState())
    val uiState: StateFlow<SchemesUiState> = _uiState.asStateFlow()

    init {
        // If you restore a query, you might want to trigger fetch with it
//        if (_currentQuery.value.isNotEmpty()) {
//            fetchMFSchemes(query = _currentQuery.value)
//        } else {
            fetchMFSchemes()
//        }

        // Save query changes to SavedStateHandle
//        _currentQuery.onEach { query ->
//            savedStateHandle[SAVED_QUERY_KEY] = query
//        }.launchIn(viewModelScope)
    }

    fun fetchMFSchemes(forceRefresh: Boolean = false) {
        // Update isLoading state immediately when starting the fetch,
        // especially for initial load or manual refresh.
        // The .onStart{} below is also good for this.
        if (forceRefresh || _uiState.value.schemes.isEmpty()) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        }
        schemeRepository.observeSchemeList(isForceRefresh = forceRefresh)
            .onStart {
                // Set loading state at the very beginning of the flow collection
                // This handles the initial load case well.
                // We ensure isLoading is false initially in SchemesUiState to avoid UI flicker
                // if data is already cached and loads instantly.
                if (_uiState.value.schemes.isEmpty()) { // Only show global loading if no data is present
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                }
            }
            .onEach { resource ->
                _uiState.value = when (resource) {
                    is NetworkResult.Loading -> {
                        // This state from repository might be for background updates.
                        // Only show global loading if no data is currently displayed.
                        _uiState.value.copy(
                            isLoading = _uiState.value.schemes.isEmpty(),
                            errorMessage = null
                        )
                    }
                    is NetworkResult.Success -> {
                        _uiState.value.copy(
                            isLoading = false,
                            schemes = resource.data,
                            // Clear error message on success
                            errorMessage = if (resource.data.isEmpty()) "No schemes found." else null
                        )
                    }
                    is NetworkResult.Error -> {
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                            // Optionally, keep displaying old data if resource.data is null and old data exists
                            // schemes = resource.data ?: _uiState.value.schemes
                        )
                    }
                }
            }
            .catch { e -> // Catch any unexpected errors from the flow itself
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "An unexpected error occurred: ${e.message}"
                )
            }
            .launchIn(viewModelScope) // Collect the Flow in the ViewModel's scope
    }


    // Example function for pull-to-refresh or explicit refresh button
    fun refreshSchemes() {
        fetchMFSchemes(forceRefresh = true)
    }

    fun clearLocalCache() {
        viewModelScope.launch {
            schemeRepository.clearCache()
            // Optionally, re-fetch or update UI to reflect empty state if desired
            // fetchMFSchemes(forceRefresh = false) // This would try to fetch from network again if cache is empty
            _uiState.value =
                SchemesUiState(errorMessage = "Cache cleared. Pull to refresh or try again.") // Reset to a clean state        }
        }
    }

    // `onCleared` is fine as is, especially if Koin manages the lifecycle of `SchemeRepository`
    // and its dependencies (like an HttpClient).
    override fun onCleared() {
        super.onCleared()
        // No need to manually close Koin-managed singletons here.
        //mfService.closeClient() // Close the Ktor client when ViewModel is no longer needed
        // It's generally better if the lifecycle of singletons (like HttpClient via MFService)
        // is managed by Koin itself rather than explicitly closing here.
        // If mfService.client is a true singleton, closing it here could affect other parts
        // of the app if they were also using it (though less likely with ViewModel scope).
        // Consider removing this if Koin manages HttpClient as a global singleton.
        // mfService.closeClient()
        //Log.d("MainViewModel", "onCleared called.")
    }

}
