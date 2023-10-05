package de.genericandwildcard.scopedserviceexample.navigation.stack

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import de.genericandwildcard.scopedserviceexample.navigation.destination.Destination
import kotlinx.coroutines.flow.StateFlow

/**
 * Contains a stack of screens and an ability to mutate it.
 */
interface NavigationState {
    val destinations: StateFlow<List<Destination>>

    fun mutate(block: BackStack.() -> BackStack)
}

val LocalNavigationState = staticCompositionLocalOf<NavigationState> {
    error("No NavigationState provided.")
}

/**
 * Implementation of [NavigationState] that saves its current state into an
 * Android [SavedStateHandle].
 */
class SavedStateNavigationState(
    private val stateKey: String,
    private val savedState: SavedStateHandle,
    initial: BackStack = emptyList(),
) : NavigationState {
    override val destinations = savedState.getStateFlow(stateKey, initial)

    override fun mutate(block: BackStack.() -> BackStack) {
        savedState[stateKey] = block(destinations.value)
    }
}