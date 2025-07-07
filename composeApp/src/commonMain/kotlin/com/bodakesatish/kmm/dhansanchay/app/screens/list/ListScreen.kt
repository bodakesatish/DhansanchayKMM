package com.bodakesatish.kmm.dhansanchay.app.screens.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ListScreen(
    navigateToDetails: (objectId: Int) -> Unit
) {
    val viewModel = koinViewModel<ListViewModel>()
    val objects by viewModel.uiState.collectAsStateWithLifecycle()
    AnimatedContent(objects.schemes) { objectsAvailable ->
        ObjectGrid(
            objects = objects.schemes,
            onObjectClick = navigateToDetails,
        )
    }
}

@Composable
private fun ObjectGrid(
    objects: List<SchemeNetworkModel>,
    onObjectClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn (
        modifier = modifier.fillMaxSize(),
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
    ) {
        items(objects, key = { it.schemeCode }) { obj ->
            ObjectFrame(
                obj = obj,
                onClick = { onObjectClick(obj.schemeCode) },
            )
        }
    }
}

@Composable
private fun ObjectFrame(
    obj: SchemeNetworkModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Spacer(Modifier.height(2.dp))

        Text(obj.schemeName, style = MaterialTheme.typography.titleMedium)
        Text(obj.schemeCode.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(obj.schemeName, style = MaterialTheme.typography.bodySmall)
    }
}