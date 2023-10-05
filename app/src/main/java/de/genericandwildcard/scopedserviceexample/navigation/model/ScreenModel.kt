package de.genericandwildcard.scopedserviceexample.navigation.model

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable

/**
 * Special type of [Closeable] that comes with a [CoroutineScope].
 * To be used as a ScreenModel.
 */
interface ScreenModel : Closeable {
    @CallSuper
    override fun close() {
        closeCoroutineScope()
    }
}

val ScreenModel.scope: CoroutineScope
    get() = screenCoroutineScopesRegistry.getOrPut(this) {
        CoroutineScope(SupervisorJob())
    }

private val screenCoroutineScopesRegistry = mutableMapOf<ScreenModel, CoroutineScope>()

private fun ScreenModel.closeCoroutineScope() =
    screenCoroutineScopesRegistry
        .remove(this)
        ?.cancel()