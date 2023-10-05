package de.genericandwildcard.scopedserviceexample.navigation.stack

import de.genericandwildcard.scopedserviceexample.example.EntryDestination
import de.genericandwildcard.scopedserviceexample.navigation.destination.Destination

val NavigationState.isEmpty
    get() = destinations.value.isEmpty()

fun NavigationState.push(destination: Destination) = mutate {
    this + destination
}

fun NavigationState.pop() = mutate {
    dropLast(1)
}

fun NavigationState.popAll() = mutate {
    emptyList()
}

fun NavigationState.backToStart() = mutate {
    listOf(EntryDestination)
}