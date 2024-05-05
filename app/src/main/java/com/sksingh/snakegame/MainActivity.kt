package com.sksingh.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.sksingh.snakegame.navigations.NavGraph
import com.sksingh.snakegame.ui.theme.SnakeGameTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeGameTheme {
                val navController = rememberNavController()



                NavGraph(navController = navController)
            
            
            //                GameScreen(
//                    state = state,
//                    onEvent = viewModel::onEvent,
//                    navController = navController 
//
//                )

            }
        }
    }

}

