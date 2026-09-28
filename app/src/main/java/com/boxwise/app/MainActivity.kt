package com.boxwise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boxwise.app.ui.BoxDetailScreen
import com.boxwise.app.ui.BoxesScreen
import com.boxwise.app.ui.QrLabelScreen
import com.boxwise.app.ui.SearchScreen
import com.boxwise.app.ui.theme.BoxwiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoxwiseTheme {
                val app = application as BoxwiseApp
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "boxes") {
                    composable("boxes") {
                        BoxesScreen(
                            dao = app.db.boxDao(),
                            onOpenBox = { nav.navigate("box/$it") },
                            onSearch = { nav.navigate("search") }
                        )
                    }
                    composable("box/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toLongOrNull()
                            ?: return@composable
                        BoxDetailScreen(
                            boxId = id,
                            db = app.db,
                            onShowLabel = { nav.navigate("label/$id") },
                            onBack = { nav.popBackStack() }
                        )
                    }
                    composable("search") {
                        SearchScreen(
                            dao = app.db.boxDao(),
                            onBack = { nav.popBackStack() }
                        )
                    }
                    composable("label/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toLongOrNull()
                            ?: return@composable
                        QrLabelScreen(
                            boxId = id,
                            db = app.db,
                            onBack = { nav.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
