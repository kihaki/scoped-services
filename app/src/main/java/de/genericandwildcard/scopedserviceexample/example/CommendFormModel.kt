package de.genericandwildcard.scopedserviceexample.example

import de.genericandwildcard.scopedserviceexample.navigation.model.ScreenModel
import de.genericandwildcard.scopedserviceexample.navigation.service.ServiceFactory
import de.genericandwildcard.scopedserviceexample.navigation.service.ServiceScope
import kotlinx.coroutines.flow.MutableStateFlow

object CommendFormScope : ServiceScope {
    private val validDestinations = setOf(CommendReasonDestination, CommendDetailsDestination)
    override fun ServiceScope.Context.isAlive(): Boolean =
        stack.any { validDestinations.contains(it) }
}

class CommendFormModel : ScreenModel {
    object Factory : ServiceFactory<CommendFormModel> {
        override fun ServiceFactory.Context.create(): CommendFormModel = CommendFormModel()
    }

    val reason = MutableStateFlow<CommendReason?>(null)
    val details = MutableStateFlow("")
}

