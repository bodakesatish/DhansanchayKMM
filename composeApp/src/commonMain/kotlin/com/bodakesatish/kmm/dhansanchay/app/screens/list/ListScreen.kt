package com.bodakesatish.kmm.dhansanchay.app.screens.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button // For a retry button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel
import org.koin.compose.viewmodel.koinViewModel
// If you create a specific Res class for strings:
// import dhansanchay_kmm.shared.generated.resources.Res
// import org.jetbrains.compose.resources.stringResource

@Composable
fun ListScreen(
    navigateToDetails: (objectId: Long, scheme: SchemeModel) -> Unit,
    modifier: Modifier = Modifier, // Add modifier parameter
) {
    val viewModel = koinViewModel<ListViewModel>()
    // Use the screen's modifier
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // AnimatedContent can be used for switching between Loading/Content/Error states
    // However, for a simpler approach, we can use if/else for clarity here.
    // AnimatedContent is great for the list items themselves appearing/disappearing.

    Box(modifier = modifier.fillMaxSize()) { // Use the passed modifier
        when {
            uiState.isLoading && uiState.schemes.isEmpty() -> { // Full screen loader only if no data yet
                LoadingIndicator(Modifier.align(Alignment.Center))
            }
            uiState.errorMessage != null && uiState.schemes.isEmpty() -> { // Error and no data
                ErrorState(
                    message = uiState.errorMessage ?: "An unknown error occurred.", // Provide a default
                    onRetry = { viewModel.fetchMFSchemes(forceRefresh = true) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            !uiState.isLoading && uiState.schemes.isEmpty() && uiState.errorMessage == null -> { // No data, not loading, no error
                EmptyState(
                    message = "No schemes available at the moment.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> { // Data is available (potentially with a background loading or error for specific items)
                // If there's an error message but we also have data, you might show a Snackbar or a small error banner
                // instead of replacing the whole screen. For now, we prioritize showing data if available.

                // AnimatedContent for the list itself, to animate changes in the list data
                AnimatedContent(
                    targetState = uiState.schemes,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "schemeListAnimation"
                ) { schemesToShow ->
                    ObjectGrid(
                        objects = schemesToShow,
                        onObjectClick = navigateToDetails,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier.wrapContentSize(Alignment.Center))
}

@Composable
private fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
            // Text(stringResource(Res.string.retry)) // Using compose resources
        }
    }
}


@Composable
private fun ObjectGrid(
    objects: List<SchemeModel>,
    onObjectClick: (Long, SchemeModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), // This modifier is from the AnimatedContent
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
    ) {
        // You might want to add a header item here if there's a uiState.errorMessage
        // and uiState.schemes is not empty (e.g., to show a non-blocking error)

        items(items = objects, key = { it.schemeCode }) { obj ->
            ObjectFrame(
                obj = obj,
                onClick = { onObjectClick(obj.schemeCode, obj) },
            )
        }
    }
}

@Composable
private fun ObjectFrame(
    obj: SchemeModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier // Use the modifier passed from LazyColumn item scope
            .fillMaxWidth() // Ensure it takes full width for clickability
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted padding

    ) {
        Text(
            text = obj.schemeName,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2 // Prevent overly long names from breaking layout
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Code: ${obj.schemeCode}", // Adding context
            style = MaterialTheme.typography.bodyMedium
        )
        // The third Text was identical to the first, maybe you intended something else?
        // For example, another piece of data or a category.
        // If it was just for spacing or styling, a Spacer or different Text style might be better.
        // Text(obj.schemeCategory ?: "", style = MaterialTheme.typography.bodySmall) // Example if you have category
    }
}
