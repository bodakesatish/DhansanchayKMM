package com.bodakesatish.kmm.dhansanchay.app.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel

// import org.koin.core.parameter.parametersOf // For passing nav argument to ViewModel

// Placeholder: In a real app, this would come from a DetailViewModel
data class SchemeDetailScreenUiState(
    val isLoading: Boolean = true,
    val schemeDetails: SchemeModel? = null,
    val errorMessage: String? = null
)

// Placeholder ViewModel - replace with your actual ViewModel later
// @Composable
// fun DetailScreen(
//    schemeId: Long, // Assume this is passed via navigation
//    onNavigateUp: () -> Unit,
//    modifier: Modifier = Modifier,
// ) {
//    // If you have a ViewModel that takes schemeId:
//    // val viewModel = koinViewModel<DetailViewModel>(parameters = { parametersOf(schemeId) })
//    // val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    // For now, using placeholder state:
//    val placeholderUiState = SchemeDetailScreenUiState(
//        isLoading = false,
//        schemeDetails = SchemeModel(
//            schemeCode = schemeId,
//            schemeName = "Amazing Growth Fund ( Placeholder)",
//            fundHouse = "Future Investments Ltd.",
//            schemeCategory = "Equity: Large Cap",
//            launchDate = "15 Mar 2018",
//            netAssetValue = 125.78,
//            lastUpdatedNAVDate = "28 Jul 2024",
//            objective = "To achieve long-term capital appreciation by investing primarily in a diversified portfolio of equity and equity-related securities of large-cap companies. The fund aims to outperform its benchmark through active stock selection and prudent risk management."
//        )
//    )
//    val uiState = placeholderUiState // Use this for now
//
//    DetailScreenContent(
//        uiState = uiState,
//        onNavigateUp = onNavigateUp,
//        modifier = modifier
//    )
// }


// Simplified version for now, assuming you'll pass SchemeModel directly or fetch it
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    // schemeId: Long, // If you fetch details based on ID
//    scheme: SchemeModel, // Or pass the whole model if already fetched
    schemeCode: Long,
    schemeName: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // This is a basic DetailScreen. You'll likely want a ViewModel to fetch details by ID.
    // For this example, we'll assume 'scheme' is passed directly or you have a simple placeholder.

    val currentScheme = SchemeModel( // Fallback for preview or if null is passed
        schemeCode = schemeCode,
        schemeName = schemeName
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScheme.schemeName, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        // If you were loading from a ViewModel:
        // when {
        //    uiState.isLoading -> Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        //    uiState.errorMessage != null -> Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { Text("Error: ${uiState.errorMessage}") }
        //    uiState.schemeDetails != null -> SchemeDetailsContent(uiState.schemeDetails!!, Modifier.padding(paddingValues))
        //    else -> Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { Text("No data available") }
        // }

        if (schemeName == null && currentScheme.schemeCode == 0L) { // If truly no data (not just placeholder)
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(currentScheme.schemeName ?: "Details are not available.")
            }
        } else {
            SchemeDetailsContent(
                scheme = currentScheme,
                modifier = Modifier
                    .padding(paddingValues) // Apply padding from Scaffold
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Make content scrollable
                    .padding(16.dp) // Inner padding for the content
            )
        }
    }
}

@Composable
private fun SchemeDetailsContent(
    scheme: SchemeModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between sections
    ) {
        // Header Section (already in TopAppBar, but you could add more here)
        // Text(
        //    text = scheme.schemeName,
        //    style = MaterialTheme.typography.headlineSmall,
        //    color = MaterialTheme.colorScheme.primary
        // )
        // Spacer(Modifier.height(8.dp))

        DetailSectionCard {
//            DetailItem(label = "Fund House", value = scheme.fundHouse)
//            DetailItem(label = "Category", value = scheme.schemeCategory)
            DetailItem(label = "Scheme Code", value = scheme.schemeCode.toString())
            DetailItem(label = "Scheme Name", value = scheme.schemeName.toString())
//            DetailItem(label = "Launch Date", value = scheme.launchDate)
        }

//        scheme.netAssetValue?.let { nav ->
            DetailSectionCard {
                DetailItem(label = "Net Asset Value (NAV)", value = "₹ 4.6545")
//                scheme.lastUpdatedNAVDate?.let {
                    DetailItem(label = "NAV As On", value = "7-Jul")
//                }
            }
//        }

//        scheme.objective?.let {
            DetailSectionCard {
                Text(
                    text = "Investment Objective",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
//        }

        // Add more sections as needed (e.g., historical performance, asset allocation etc.)
        Spacer(Modifier.height(16.dp)) // Extra space at the bottom
    }
}

@Composable
private fun DetailSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


// --- How you might call it from your Navigation setup ---
// NavHost(...) {
//    composable(
//        route = "list_screen",
//    ) {
//        ListScreen(
//            navigateToDetails = { schemeId ->
//                // You'd typically pass the ID, and DetailScreen's ViewModel would fetch
//                // For now, if your ListScreen can pass the full SchemeModel:
//                // navController.navigateToDetailScreen(schemeModel) // hypothetical extension
//                navController.navigate("detail_screen/$schemeId")
//            }
//        )
//    }
//    composable(
//        route = "detail_screen/{schemeId}",
//        arguments = listOf(navArgument("schemeId") { type = NavType.LongType })
//    ) { backStackEntry ->
//        val schemeId = backStackEntry.arguments?.getLong("schemeId")
//        // Here, you'd ideally have a DetailViewModel that takes schemeId
//        // and fetches the data.
//        // For this example, let's assume you retrieve the SchemeModel by ID
//        // from a repository or a shared ViewModel, or pass it.
//        // This is a simplified way to get a placeholder for now:
//        val placeholderScheme = SchemeModel(schemeId ?: 0, "Details for $schemeId", /*...other fields */)
//
//        DetailScreen(
//            // schemeId = schemeId ?: 0L, // Pass ID to DetailScreen/ViewModel
//            scheme = placeholderScheme, // Replace with actual fetched scheme
//            onNavigateUp = { navController.popBackStack() }
//        )
//    }
// }

