package com.rebelfocus.features.apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rebelfocus.core.common.util.toImageBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPickerScreen(
    onNavigateBack: () -> Unit,
    viewModel: AppPickerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Apps (${state.selectedCount})") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.saveSelections()
                        onNavigateBack()
                    }) { Text("Save") }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search apps…") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.filteredApps, key = { it.info.packageName }) { app ->
                        val icon = remember(app.info.packageName) {
                            try {
                                context.packageManager
                                    .getApplicationIcon(app.info.packageName)
                                    .toImageBitmap()
                            } catch (_: Exception) { null }
                        }
                        ListItem(
                            headlineContent = { Text(app.info.appName) },
                            supportingContent = { Text(app.info.packageName,
                                style = MaterialTheme.typography.bodySmall) },
                            leadingContent = {
                                if (icon != null) {
                                    Image(bitmap = icon, contentDescription = null,
                                        modifier = Modifier.size(40.dp))
                                } else {
                                    Box(Modifier.size(40.dp))
                                }
                            },
                            trailingContent = {
                                Checkbox(checked = app.isSelected,
                                    onCheckedChange = { viewModel.toggleApp(app.info.packageName) })
                            },
                            modifier = Modifier.clickable {
                                viewModel.toggleApp(app.info.packageName)
                            }
                        )
                    }
                }
            }
        }
    }
}
