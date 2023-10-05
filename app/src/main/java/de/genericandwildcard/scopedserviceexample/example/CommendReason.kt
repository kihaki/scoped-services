package de.genericandwildcard.scopedserviceexample.example

import de.genericandwildcard.scopedserviceexample.example.CommendReason.MOSTLY_CORRECT
import de.genericandwildcard.scopedserviceexample.example.CommendReason.SUPER_SMART
import de.genericandwildcard.scopedserviceexample.example.CommendReason.VERY_COOL

val CommendReason?.label: String
    get() = when (this) {
        VERY_COOL -> "Tweet was very cool"
        SUPER_SMART -> "Tweet was very smart"
        MOSTLY_CORRECT -> "Tweet was indisputably factually correct"
        null -> "None"
    }

enum class CommendReason {
    VERY_COOL, SUPER_SMART, MOSTLY_CORRECT
}