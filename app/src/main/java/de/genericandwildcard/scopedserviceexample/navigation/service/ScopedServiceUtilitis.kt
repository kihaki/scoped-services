package de.genericandwildcard.scopedserviceexample.navigation.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import de.genericandwildcard.scopedserviceexample.navigation.destination.Destination
import de.genericandwildcard.scopedserviceexample.navigation.destination.Screen
import de.genericandwildcard.scopedserviceexample.navigation.stack.BackStack

val LocalScopedServicesCache = staticCompositionLocalOf<ServicesState> {
    error("No LocalScopedServicesCache provided.")
}

@Composable
inline fun <reified T : Any> Screen.screenScopedService(
    factory: ServiceFactory<T>,
    servicesCache: ServicesState = LocalScopedServicesCache.current,
) = scopedService(
    scope = DestinationScoped(destination),
    factory = factory,
    servicesCache = servicesCache,
)

/**
 * The service should be alive for as long as the provided destination is anywhere on the stack.
 */
data class DestinationScoped(val destinations: Set<Destination>) : ServiceScope {
    constructor(vararg keys: Destination) : this(keys.toSet())

    override fun ServiceScope.Context.isAlive(): Boolean =
        stack.containsKey(destinations)

    private fun BackStack.containsKey(keys: Set<Destination>) =
        any { keys.contains(it) }
}

@Composable
inline fun <reified T : Any> scopedService(
    scope: ServiceScope,
    factory: ServiceFactory<T>,
    servicesCache: ServicesState = LocalScopedServicesCache.current,
) = servicesCache.get(scope = scope, factory = factory)