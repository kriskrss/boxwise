package com.boxwise.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boxwise.app.data.Box
import com.boxwise.app.data.BoxwiseDatabase
import com.boxwise.app.data.Item
import kotlinx.coroutines.launch

@Composable
fun BoxDetailScreen(
    boxId: Long,
    db: BoxwiseDatabase,
    onShowLabel: () -> Unit,
    onBack: () -> Unit
) {
    var box by remember { mutableStateOf<Box?>(null) }
    val items by db.itemDao().observeItems(boxId).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showAdd by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("1") }

    LaunchedEffect(boxId) { box = db.boxDao().getBox(boxId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(box?.label ?: "Box") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onShowLabel) {
                        Icon(Icons.Default.QrCode2, contentDescription = "QR label")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add item")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    supportingContent = {
                        Text("× ${item.quantity}" + (item.notes?.let { " — $it" } ?: ""))
                    }
                )
                HorizontalDivider()
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Add item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Item name") }
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = qty,
                        onValueChange = { qty = it },
                        label = { Text("Quantity") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val q = qty.toIntOrNull() ?: 1
                    scope.launch {
                        db.itemDao().insert(Item(boxId = boxId, name = name, quantity = q))
                    }
                    name = ""
                    qty = "1"
                    showAdd = false
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAdd = false }) { Text("Cancel") }
            }
        )
    }
}
