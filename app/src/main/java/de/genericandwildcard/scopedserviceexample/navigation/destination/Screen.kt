package de.genericandwildcard.scopedserviceexample.navigation.destination

import androidx.compose.runtime.Composable

interface Screen {
    val destination: Destination

    @Composable
    fun Content()
}