package de.genericandwildcard.scopedserviceexample.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import de.genericandwildcard.scopedserviceexample.example.EntryDestination
import de.genericandwildcard.scopedserviceexample.navigation.service.InMemoryServicesState
import de.genericandwildcard.scopedserviceexample.navigation.service.ServicesState
import de.genericandwildcard.scopedserviceexample.navigation.stack.NavigationState
import de.genericandwildcard.scopedserviceexample.navigation.stack.SavedStateNavigationState

class NavigationViewModel(handle: SavedStateHandle) : ViewModel() {
    val navigationState: NavigationState = SavedStateNavigationState(
        stateKey = "default",
        savedState = handle,
        initial = listOf(EntryDestination)
    )
    val servicesCache: ServicesState = InMemoryServicesState(navigationState)

    override fun onCleared() {
        super.onCleared()
        servicesCache.disposeDeadServices()
    }
}