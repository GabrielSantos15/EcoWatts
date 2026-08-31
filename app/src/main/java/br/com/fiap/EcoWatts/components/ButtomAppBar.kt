package br.com.fiap.EcoWatts.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun ButtomAppBar(
    navController: NavController,
    currentRoute: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavigationItem(stringResource(R.string.home), icon = Icons.Default.Home, route = Destination.HomeScreen.route),
        BottomNavigationItem(stringResource(R.string.appliances), icon = Icons.Default.List, route = Destination.AppliancesScreen.route),
        BottomNavigationItem(stringResource(R.string.dashboard), icon = Icons.Default.Dashboard, route = Destination.DashboardScreen.route),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Preview
@Composable
private fun ButtomAppBarPreview() {
    EcoWatssTheme() {
        ButtomAppBar(rememberNavController(),"")
    }
}