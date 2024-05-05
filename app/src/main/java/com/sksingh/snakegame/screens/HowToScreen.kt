package com.sksingh.snakegame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.sksingh.snakegame.R
import com.sksingh.snakegame.navigations.Routes

@Composable
fun HowToScreen(navController: NavController) {
    val images = listOf(
        R.drawable.rightmove,
        R.drawable.downmove,
        R.drawable.leftmove,
        R.drawable.topmove
    )
    var currentImageIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        // Force recomposition when currentImageIndex changes
    ) {
        Image(
            painter = painterResource(id = images[currentImageIndex]),
            contentDescription = "Image ${currentImageIndex + 1}",
            modifier = Modifier.clickable {
                // You can handle click on the image if needed
                if (currentImageIndex == images.size-1){
                    navController.navigate(Routes.Game.routes){
                        popUpTo(Routes.Splash.routes) {

                        }
                    }

                }
                currentImageIndex = (currentImageIndex + 1) % images.size
            }
                , contentScale = ContentScale.Crop
        )

//        // Right arrow to navigate to the next image
//        Image(
//            imageVector = Icons.Rounded.ArrowForward,
//            contentDescription = "Next",
//            modifier = Modifier.clickable {
//                if (currentImageIndex == images.size-1){
//                    navController.navigate(Routes.Game.routes)
//
//                }
//                currentImageIndex = (currentImageIndex + 1) % images.size
//
//
//            }
//                .size(90.dp)
//        )
    }
}