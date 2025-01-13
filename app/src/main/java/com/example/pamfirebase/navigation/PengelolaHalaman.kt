package com.example.pamfirebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentManager.BackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pamfirebase.ui.theme.pages.DestinasiDetail
import com.example.pamfirebase.ui.theme.pages.DetailView
import com.example.pamfirebase.ui.theme.pages.HomeScreen
import com.example.pamfirebase.ui.theme.pages.InsertMhsView

@Composable
fun PengelolaHalaman (
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost (
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier
    ) {
        composable(route = DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiInsert.route) }
            )
        }

        composable(route = DestinasiInsert.route) {
            InsertMhsView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(DestinasiHome.route) }
            )
        }
    }
}