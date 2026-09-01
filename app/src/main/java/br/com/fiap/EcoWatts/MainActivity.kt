package br.com.fiap.EcoWatts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.fiap.EcoWatts.navigation.NavigationRoutes
import br.com.fiap.EcoWatts.screens.InitialScreen
import br.com.fiap.EcoWatts.screens.LoginScreen
import br.com.fiap.EcoWatts.screens.SignupScreen
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoWatssTheme {
                NavigationRoutes()
            }
        }
    }
}
