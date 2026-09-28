package com.boxwise.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boxwise.app.data.BoxDao
import kotlinx.coroutines.flow.flowOf

/** Global search: "where is X?" across every box. */
@Composable
fun SearchScreen(dao: BoxDao, onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val results by remember(query) {
        if (query.isBlank()) flowOf(emptyList()) else dao.searchItems(query)
    }.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Where is…?") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            LazyColumn(contentPadding = PaddingValues(top = 8.dp)) {
                items(results) { r ->
                    ListItem(
                        headlineContent = { Text(r.item.name) },
                        supportingContent = { Text("× ${r.item.quantity} — in ${r.boxLabel}") }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
