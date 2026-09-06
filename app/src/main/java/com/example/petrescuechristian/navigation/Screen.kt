package com.example.petrescuechristian.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")

    object Catalog : Screen("catalog?category={category}") {
        fun createRoute(category: String? = null): String =
            if (category != null) "catalog?category=$category" else "catalog"
    }

    object PetDetail : Screen("petDetail/{petId}") {
        fun createRoute(petId: Int): String = "petDetail/$petId"
    }

    object Report : Screen("report")

    object ReportDetail : Screen("reportDetail/{reportId}") {
        fun createRoute(reportId: Int): String = "reportDetail/$reportId"
    }

    object Nearby : Screen("nearby")
    object Favorites : Screen("favorites")
    object MyReports : Screen("myReports")
}
