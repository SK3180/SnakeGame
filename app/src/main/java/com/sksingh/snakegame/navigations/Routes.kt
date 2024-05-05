package com.sksingh.snakegame.navigations

sealed class Routes(val routes:String){

    data object Splash :  Routes("splash")
    data object How :  Routes("how")
    data object Game :  Routes("game")
}
