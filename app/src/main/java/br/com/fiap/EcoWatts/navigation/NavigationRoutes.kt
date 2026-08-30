package br.com.fiap.EcoWatts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.screens.AddApplianceScreen
import br.com.fiap.EcoWatts.screens.HomeScreen
import br.com.fiap.EcoWatts.screens.InitialScreen
import br.com.fiap.EcoWatts.screens.LoginScreen
import br.com.fiap.EcoWatts.screens.AppliancesScreen
import br.com.fiap.EcoWatts.screens.ProfileScreen
import br.com.fiap.EcoWatts.screens.SignupScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController();

    val context = LocalContext.current
    val sessionRepository = SessionRepository(context)

    val isUserLoggedIn = sessionRepository.getUserId() != 0

    val startScreen = if (isUserLoggedIn) {
        Destination.HomeScreen.route
    } else {
        Destination.InitialScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startScreen
    ) {
        composable(Destination.InitialScreen.route){ InitialScreen(navController) }
        composable(Destination.HomeScreen.route){ HomeScreen(navController) }
        composable(Destination.SignupScreen.route) { SignupScreen(navController) }
        composable(Destination.LoginScreen.route) { LoginScreen(navController) }

        composable(
            route = Destination.AddApplianceScreen.route,
            arguments = listOf(
                navArgument("applianceId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val applianceId = backStackEntry.arguments?.getInt("applianceId") ?: -1
            AddApplianceScreen(
                navController = navController,
                applianceId = if (applianceId == -1) null else applianceId
            )
        }

        composable(Destination.AppliancesScreen.route) { AppliancesScreen(navController) }
        composable(Destination.ProfileScreen.route) { ProfileScreen(navController) }
    }
}