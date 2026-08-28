package br.com.fiap.EcoWatts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.screens.HomeScreen
import br.com.fiap.EcoWatts.screens.InitialScreen
import br.com.fiap.EcoWatts.screens.LoginScreen
import br.com.fiap.EcoWatts.screens.SignupScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController();
    NavHost(
        navController = navController,
        startDestination = Destination.InitialScreen.route
    ) {
        composable(Destination.InitialScreen.route){ InitialScreen(navController) }
        composable(Destination.HomeScreen.route){ HomeScreen() }
        composable(Destination.SignupScreen.route) { SignupScreen(navController) }
        composable(Destination.LoginScreen.route) { LoginScreen(navController) }
    }
}