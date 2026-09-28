package com.boxwise.app.ui

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.boxwise.app.data.BoxwiseDatabase
import com.boxwise.app.util.qrCodeBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Printable QR label for a box. The QR encodes `boxwise://box/{id}`.
 * "Share" exports a PNG — the Android share sheet includes printers,
 * so this doubles as the print path for v0.1.
 */
@Composable
fun QrLabelScreen(boxId: Long, db: BoxwiseDatabase, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var label by remember { mutableStateOf("Box $boxId") }

    LaunchedEffect(boxId) {
        label = db.boxDao().getBox(boxId)?.label ?: "Box $boxId"
    }

    val payload = "boxwise://box/$boxId"
    val qr = remember(payload) { qrCodeBitmap(payload) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Label — $label") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch(Dispatchers.IO) {
                            val file = File(context.cacheDir, "shared/box-$boxId-label.png")
                            file.parentFile?.mkdirs()
                            file.outputStream().use {
                                qr.asAndroidBitmap()
                                    .compress(Bitmap.CompressFormat.PNG, 100, it)
                            }
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                            val send = Intent(Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            withContext(Dispatchers.Main) {
                                context.startActivity(
                                    Intent.createChooser(send, "Share label")
                                )
                            }
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share label")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Image(
                bitmap = qr,
                contentDescription = "QR code for $label",
                modifier = Modifier.size(280.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Stick on the box. Scan to open it.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
