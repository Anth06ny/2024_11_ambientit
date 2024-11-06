package com.amonteiro.a2024_11_ambientit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amonteiro.a2024_11_ambientit.ui.screens.DetailScreen
import com.amonteiro.a2024_11_ambientit.ui.screens.SearchScreen
import com.amonteiro.a2024_11_ambientit.viewmodel.MainViewModel
import com.amonteiro.a2024_11_ambientit.viewmodel.PictureBean

sealed class Routes(val route: String) {
    data object SearchScreen : Routes("searchScreen")
    data object DetailScreen : Routes("detailScreen/{id}") {
        fun withId(id: Int) = "detailScreen/id"
        fun withObject(data: PictureBean) = "detailScreen/${data.id}"
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navHostController: NavHostController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    NavHost(navController = navHostController, startDestination = Routes.SearchScreen.route, modifier = modifier) {
        //Route 1
        composable(Routes.SearchScreen.route) {
            SearchScreen(
                mainViewModel = mainViewModel,
                onPictureRowItemClick = {
                    navHostController.navigate(Routes.DetailScreen.withObject(it))
                }
            )
        }

        //Route 2
        composable(
            route = Routes.DetailScreen.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 1
            DetailScreen(
                idPicture = id,
                mainViewModel = mainViewModel,
                onBackClick = {
                    navHostController.popBackStack()
                },
                backStack = navHostController.previousBackStackEntry != null,
                onBackIconClick = {navHostController.popBackStack()}
            )
        }
    }
}