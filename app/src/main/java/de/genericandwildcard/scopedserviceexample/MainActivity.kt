package de.genericandwildcard.scopedserviceexample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import de.genericandwildcard.scopedserviceexample.navigation.NavigationViewModel
import de.genericandwildcard.scopedserviceexample.navigation.destination.Screen
import de.genericandwildcard.scopedserviceexample.navigation.service.LocalScopedServicesCache
import de.genericandwildcard.scopedserviceexample.navigation.stack.NavigationState
import de.genericandwildcard.scopedserviceexample.navigation.stack.LocalNavigationState
import de.genericandwildcard.scopedserviceexample.navigation.stack.pop
import de.genericandwildcard.scopedserviceexample.ui.theme.ScopedServiceExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScopedServiceExampleTheme {
                val navigationViewModel: NavigationViewModel = viewModel()

                CompositionLocalProvider(
                    LocalNavigationState provides navigationViewModel.navigationState,
                    LocalScopedServicesCache provides navigationViewModel.servicesCache,
                ) {
                    PopOnBackpressHandler()

                    ScreenTransition { screen ->
                        screen.Content()
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenTransition(
    render: @Composable (Screen) -> Unit,
) {
    val serviceCache = LocalScopedServicesCache.current
    val backStackState = LocalNavigationState.current
    val currentScreen = backStackState.CurrentScreen()

    AnimatedContent(
        targetState = currentScreen,
        label = "screen-stack",
    ) { screen ->
        screen?.let {
            render(it)
        }

        DisposableEffect(Unit) {
            onDispose {
                serviceCache.disposeDeadServices()
            }
        }
    }
}

@Composable
private fun PopOnBackpressHandler() {
    val backStackState = LocalNavigationState.current
    val currentScreen = backStackState.CurrentScreen()

    BackHandler(
        enabled = currentScreen != null,
    ) {
        backStackState.pop()
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun NavigationState.CurrentScreen(): Screen? {
    val backStack by destinations.collectAsState()
    val currentScreen by remember { derivedStateOf { backStack.lastOrNull()?.build() } }
    return currentScreen
}