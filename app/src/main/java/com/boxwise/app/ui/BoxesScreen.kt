package com.boxwise.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boxwise.app.data.Box
import com.boxwise.app.data.BoxDao
import kotlinx.coroutines.launch

@Composable
fun BoxesScreen(dao: BoxDao, onOpenBox: (Long) -> Unit, onSearch: () -> Unit) {
    val boxes by dao.observeBoxes().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showAdd by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Boxwise") },
                actions = {
                    IconButton(onClick = onSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add box")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(boxes) { box ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable { onOpenBox(box.id) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(box.label, style = MaterialTheme.typography.titleMedium)
                        box.location?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("New box") },
            text = {
                Column {
                    OutlinedTextField(
                        value = label,
                        onValueChange = { label = it },
                        label = { Text("Label (e.g. Box 12)") }
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location (optional)") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        dao.insert(Box(label = label, location = location.ifBlank { null }))
                    }
                    label = ""
                    location = ""
                    showAdd = false
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAdd = false }) { Text("Cancel") }
            }
        )
    }
}
