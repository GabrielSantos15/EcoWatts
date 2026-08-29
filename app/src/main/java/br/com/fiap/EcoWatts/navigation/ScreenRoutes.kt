package br.com.fiap.EcoWatts.navigation

sealed class Destination(val route: String){
    object InitialScreen: Destination("initial")
    object SignupScreen: Destination("signup")
    object HomeScreen: Destination("home")
    object LoginScreen: Destination("login")

    object MeusAparelhosScreen: Destination("meus_aparelhos")

    object AddApplianceScreen : Destination("add_appliance?applianceId={applianceId}") {
        fun createRoute(applianceId: Int? = null): String {
            return "add_appliance?applianceId=${applianceId ?: -1}"
        }
    }
}