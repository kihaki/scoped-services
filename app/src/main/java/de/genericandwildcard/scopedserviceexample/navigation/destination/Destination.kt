package de.genericandwildcard.scopedserviceexample.navigation.destination

import java.io.Serializable

interface Destination : Serializable {
    fun build(): Screen
}

