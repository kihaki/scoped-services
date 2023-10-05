package de.genericandwildcard.scopedserviceexample.navigation.service

context(ServiceScope.Context)
fun ServiceScope.isDead() = !isAlive()