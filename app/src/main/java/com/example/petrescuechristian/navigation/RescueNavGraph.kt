package com.example.petrescuechristian.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petrescuechristian.data.SessionManager
import com.example.petrescuechristian.ui.screens.catalog.CatalogScreen
import com.example.petrescuechristian.ui.screens.favorites.FavoritesScreen
import com.example.petrescuechristian.ui.screens.home.HomeScreen
import com.example.petrescuechristian.ui.screens.login.LoginScreen
import com.example.petrescuechristian.ui.screens.myreports.MyReportsScreen
import com.example.petrescuechristian.ui.screens.nearby.NearbyScreen
import com.example.petrescuechristian.ui.screens.petdetail.PetDetailScreen
import com.example.petrescuechristian.ui.screens.report.ReportScreen
import com.example.petrescuechristian.ui.screens.reportdetail.ReportDetailScreen
import com.example.petrescuechristian.ui.screens.splash.SplashScreen

@Composable
fun RescueNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { species ->
                    navController.navigate(Screen.Catalog.createRoute(species.name))
                },
                onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                },
                onSeeAllClick = {
                    navController.navigate(Screen.Catalog.createRoute())
                },
                onReportClick = {
                    navController.navigate(Screen.Report.route)
                },
                onNearbyClick = {
                    navController.navigate(Screen.Nearby.route)
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                },
                onMyReportsClick = {
                    navController.navigate(Screen.MyReports.route)
                },
                onLogout = {
                    SessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Catalog.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            CatalogScreen(
                initialCategory = category,
                onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.PetDetail.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: -1
            PetDetailScreen(
                petId = petId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Report.route) {
            ReportScreen(
                onReportCreated = { reportId ->
                    navController.navigate(Screen.ReportDetail.createRoute(reportId)) {
                        popUpTo(Screen.Report.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ReportDetail.route,
            arguments = listOf(navArgument("reportId") { type = NavType.IntType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getInt("reportId") ?: -1
            ReportDetailScreen(
                reportId = reportId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Nearby.route) {
            NearbyScreen(
                onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.MyReports.route) {
            MyReportsScreen(
                onReportClick = { reportId ->
                    navController.navigate(Screen.ReportDetail.createRoute(reportId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
