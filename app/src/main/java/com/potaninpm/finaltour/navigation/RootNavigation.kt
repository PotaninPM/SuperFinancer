package com.potaninpm.finaltour.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.potaninpm.feature_finances.screens.FinancesScreen
import com.potaninpm.feature_home.presentation.screens.HomeScreen
import com.potaninpm.finaltour.R
import com.potaninpm.finaltour.navigation.bottomNav.BottomNavBar
import com.potaninpm.finaltour.navigation.bottomNav.BottomNavItem

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithBottomBar = listOf(
        RootNavDestinations.Home.route,
        RootNavDestinations.Finances.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in routesWithBottomBar) {
                BottomNavBar(
                    navController = rootNavController,
                    destinations = listOf(
                        BottomNavItem(
                            route = RootNavDestinations.Home,
                            labelRes = R.string.home,
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.home_24px_filled),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.home_24px_not_filled)
                        ),
                        BottomNavItem(
                            route = RootNavDestinations.Finances,
                            labelRes = R.string.finances,
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.paid_24px_filled),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.paid_24px_not_filled)
                        ),
                        BottomNavItem(
                            route = RootNavDestinations.News,
                            labelRes = R.string.news,
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.newspaper_24px_filled),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.newspaper_24px_not_filled)
                        )
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = rootNavController,
            startDestination = RootNavDestinations.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(RootNavDestinations.Home.route) {
                HomeScreen()
            }

            composable(RootNavDestinations.Finances.route) {
                FinancesScreen()
            }
        }
    }

}