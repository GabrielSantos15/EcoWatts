package br.com.fiap.EcoWatts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.screens.AddApplianceScreen
import br.com.fiap.EcoWatts.screens.HomeScreen
import br.com.fiap.EcoWatts.screens.InitialScreen
import br.com.fiap.EcoWatts.screens.LoginScreen
import br.com.fiap.EcoWatts.screens.SignupScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController();

    val context = LocalContext.current
    val sessionRepository = SessionRepository(context)

    // Verificamos se está alguém logado
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
        composable(Destination.AddApplianceScreen.route) { AddApplianceScreen(navController) }
    }
}