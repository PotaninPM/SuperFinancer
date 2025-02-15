package com.potaninpm.finaltour.navigation

sealed class RootNavDestinations(val route: String) {

    object Welcome : RootNavDestinations("welcome")

    object Home : RootNavDestinations("home")

    object Search : RootNavDestinations("search")

    object Finances : RootNavDestinations("finances")

    object Feed : RootNavDestinations("feed")
}