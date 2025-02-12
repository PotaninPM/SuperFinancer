package com.potaninpm.finaltour.navigation

import kotlinx.serialization.Serializable

sealed class RootNavDestinations(val route: String) {

    object Welcome : RootNavDestinations("welcome")

    object Home : RootNavDestinations("home")

    object Finances : RootNavDestinations("finances")

    object News : RootNavDestinations("news")
}