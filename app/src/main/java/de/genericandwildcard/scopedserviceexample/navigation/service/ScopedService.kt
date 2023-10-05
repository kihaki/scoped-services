package de.genericandwildcard.scopedserviceexample.navigation.service

import android.util.Log
import de.genericandwildcard.scopedserviceexample.navigation.stack.BackStack
import de.genericandwildcard.scopedserviceexample.navigation.stack.NavigationState
import java.io.Closeable

private const val LOG_TAG = "ScopedServices"

/**
 * Defines how long a service is cached for (how long it is "alive").
 */
interface ServiceScope {
    class Context(
        val stack: BackStack,
    )

    fun Context.isAlive(): Boolean
}

/**
 * Defines how a service is created when required.
 * Also serves as a unique identifier for a service.
 */
interface ServiceFactory<T : Any> {
    class Context

    fun Context.create(): T
}

/**
 * Caches active services.
 */
interface ServicesState {
    fun <T : Any> get(
        scope: ServiceScope,
        factory: ServiceFactory<T>,
    ): T

    fun disposeDeadServices()
}

/**
 * A combined ServiceScope that manages multiple child scopes.
 */
class ServiceScopeGroup : ServiceScope {
    private val childScopes = mutableSetOf<ServiceScope>()

    override fun ServiceScope.Context.isAlive(): Boolean {
        val dead = childScopes.filter { it.isDead() }.toSet()
        childScopes.removeAll(dead)
        return childScopes.isNotEmpty()
    }

    operator fun plusAssign(scope: ServiceScope) {
        childScopes += scope
    }
}

class InMemoryServicesState(
    private val state: NavigationState,
) : ServicesState {
    private val factoryContext = ServiceFactory.Context()

    private val scopes: ServiceScopes = mutableMapOf()
    private val instances: ServiceInstances = mutableMapOf()

    override fun <T : Any> get(
        scope: ServiceScope,
        factory: ServiceFactory<T>,
    ): T {
        scopes.add(key = factory, scope = scope)

        // Return a cached instance or, if that does not exist yet,
        // create, cache and return a new instance.
        return instances.getOrPut(key = factory, context = factoryContext)
    }

    override fun disposeDeadServices() {
        val context = ServiceScope.Context(state.destinations.value)

        val dead = scopes.filter { (_, scope) ->
            with(context) {
                val isDead = scope.isDead()
                isDead
            }
        }

        dead.forEach { (scopeKey, _) ->
            (instances[scopeKey] as? Closeable)?.close()
            instances.remove(scopeKey)?.apply {
                Log.v(LOG_TAG, "Disposed Service ${this::class.java} -> ${this.hashCode()}")
            }
            scopes.remove(scopeKey)
        }
    }
}

typealias ServiceScopes = MutableMap<ServiceFactory<*>, ServiceScopeGroup>
typealias ServiceInstances = MutableMap<ServiceFactory<*>, Any>

private fun <T : Any> ServiceScopes.add(
    key: ServiceFactory<T>,
    scope: ServiceScope,
) {
    val updatedScopes = getOrDefault(key, ServiceScopeGroup()).apply {
        this += scope
    }
    put(key, updatedScopes)
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> ServiceInstances.getOrPut(
    key: ServiceFactory<T>,
    context: ServiceFactory.Context,
): T = getOrPut(key) {
    with(key) {
        context.create().apply {
            Log.v(LOG_TAG, "Created Service ${this::class.java} -> ${this.hashCode()}")
        }
    }
} as T