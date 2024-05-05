package com.sksingh.snakegame.screens


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sksingh.snakegame.R
import com.sksingh.snakegame.navigations.Routes
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController){
    val context = LocalContext.current
    SplashLogo()
    LaunchedEffect(true) {
        delay(2000)
        val shouldShowHowTo = shouldShowHowToScreen(context)
        if (shouldShowHowTo) {
            navController.navigate(Routes.How.routes)
            val preferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            preferences.edit().putBoolean("firstTime", false).apply()
        } else {
            navController.navigate(Routes.Game.routes)
        }
    }
}
fun shouldShowHowToScreen(context: Context): Boolean {
    val preferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    return preferences.getBoolean("firstTime", true)
}




@Composable
fun SplashLogo(){
    Column(modifier = Modifier.fillMaxSize()) {

        Box() {
            Image(modifier = Modifier.fillMaxSize(), painter = painterResource(id = R.drawable.splash),
                contentDescription = "SplashScreen", contentScale = ContentScale.Crop)
            Column(modifier = Modifier.fillMaxSize()
                .padding(bottom = 20.dp)
                ,
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {

            }

        }

    }

}