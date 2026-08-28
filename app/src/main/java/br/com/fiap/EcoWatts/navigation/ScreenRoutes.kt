package br.com.fiap.EcoWatts.navigation

sealed class Destination(val route: String){
    object InitialScreen: Destination("initial")
    object SignupScreen: Destination("signup")
    object HomeScreen: Destination("home")
    object LoginScreen: Destination("login")
}