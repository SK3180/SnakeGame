package com.sksingh.snakegame.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sksingh.snakegame.SnakeGameViewModel
import com.sksingh.snakegame.screens.GameScreen
import com.sksingh.snakegame.screens.HowToScreen
import com.sksingh.snakegame.screens.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel = viewModel<SnakeGameViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(Routes.Splash.routes) {
            SplashScreen(navController)
        }
        composable(Routes.How.routes) {
            HowToScreen(navController)
        }
        composable(Routes.Game.routes) {
            GameScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    navController = navController

                )
        }
    }
}
