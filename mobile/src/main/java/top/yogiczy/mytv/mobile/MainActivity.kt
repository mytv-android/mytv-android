package top.yogiczy.mytv.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.mobile.ui.screens.Screens
import top.yogiczy.mytv.mobile.ui.theme.MyTVTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, window.decorView).let { insetsController ->
                insetsController.hide(WindowInsetsCompat.Type.statusBars())
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            val navController = rememberNavController()
            val navItems = Screens.entries.filter { it.isTabItem }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            MyTVTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(Constants.APP_TITLE) },
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            navItems.forEach { screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            screen.tabIcon ?: Icons.Default.Circle,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(screen.label ?: screen.name) },
                                    selected = currentDestination?.route == screen(),
                                    onClick = {
                                        navController.navigate(screen()) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = Screens.Channels(),
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screens.Channels()) {
                            Text("频道界面")
                        }

                        composable(Screens.Favorites()) {
                            Text("收藏界面")
                        }

                        composable(Screens.Configs()) {
                            Text("配置界面")
                        }

                        composable(Screens.Settings()) {
                            Text("设置界面")
                        }
                    }
                }
            }
        }
    }
}