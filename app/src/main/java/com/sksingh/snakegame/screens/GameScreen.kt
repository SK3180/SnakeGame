package com.sksingh.snakegame.screens



import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sksingh.snakegame.Coordinate
import com.sksingh.snakegame.Direction
import com.sksingh.snakegame.GameState
import com.sksingh.snakegame.R
import com.sksingh.snakegame.SnakeGameEvent
import com.sksingh.snakegame.SnakeGameState
import com.sksingh.snakegame.ui.theme.Custard
import com.sksingh.snakegame.ui.theme.Royal


@Composable
fun GameScreen(navController: NavHostController,
    state: SnakeGameState,
    onEvent: (SnakeGameEvent) -> Unit

){
    val backgroundDrawable = painterResource(id = R.drawable.bgfinal)
    val foodImageBitmap = ImageBitmap.imageResource(id = R.drawable.food)
    val snakeHeadImageBitmap = when(state.direction){
        Direction.UP -> ImageBitmap.imageResource(id = R.drawable.up)
        Direction.DOWN ->ImageBitmap.imageResource(id = R.drawable.down)
        Direction.RIGHT ->ImageBitmap.imageResource(id = R.drawable.right)
        Direction.LEFT -> ImageBitmap.imageResource(id = R.drawable.left)
    }
    val snakebackBitmap = ImageBitmap.imageResource(id = R.drawable.circle)
      val context = LocalContext.current
    val foodSound = remember{ MediaPlayer.create(context, R.raw.eat) }
    val bgSound = remember{ MediaPlayer.create(context, R.raw.bgmusic) }
    val gameOver = remember{ MediaPlayer.create(context, R.raw.gameover) }


    LaunchedEffect(key1 = state.snake.size){
        if (state.snake.size != 1){
            foodSound?.start()
        }

    }
    LaunchedEffect(key1 = state.isGameOver){
        if (state.isGameOver){
            gameOver?.start()
            bgSound?.stop()
        }

    }
    LaunchedEffect(state.gameState) {
        if (state.gameState == GameState.STARTED) {
            bgSound?.start()
        } else if (state.gameState == GameState.PAUSED) {
            bgSound?.pause()
        }
        else
            bgSound?.start()

    }


    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){

//        Image(modifier =  Modifier.fillMaxSize(),
//            painter = backgroundDrawable,
//            contentDescription = "background", contentScale = ContentScale.Crop )

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround)
        {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {

        Image(modifier =  Modifier.fillMaxSize(),
            painter = backgroundDrawable,
           contentDescription = "background",
            contentScale = ContentScale.Crop )

                Image(
                    painter = painterResource(id = R.drawable.scorecard),
                    contentDescription = "Score Card",
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp)
                    ,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Score ${state.snake.size-1}",
                    fontSize = 35.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp), // Adjust vertical padding as needed
                    textAlign = TextAlign.Center
                )
            }

            Canvas(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .pointerInput(state.gameState) {
                    if (state.gameState != GameState.STARTED) {
                        return@pointerInput
                    }
                    detectTapGestures { offset ->
                        onEvent(SnakeGameEvent.UpdateDirection(offset, size.width))

                    }

                }
            ){
                val cellSize = size.width/20
                drawGame(
                    cellSize = cellSize,
                    cellColor = Custard,
                    borderCallColor = Royal,
                    gridHeight = state.yAxisGridSize,
                    gridWidth = state.xAxisGridSize
                )
                drawFood(foodImage = foodImageBitmap,
                    cellSize = cellSize.toInt(),
                    coordinate =state.food

                )
                drawSnake(
                    snakeHeadImage = snakeHeadImageBitmap,
                    cellSize = cellSize,
                    snake = state.snake,
                    snakeback = snakebackBitmap

                )


            }
            Button(onEvent = onEvent,state = state)





        }
        val myfont = FontFamily(Font(R.font.myfont)
        )

            AnimatedVisibility(visible = state.isGameOver)
            {
                Text(modifier = Modifier.padding(15.dp)
                    .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "GameOver",
                    fontFamily = myfont,
                    color = Color.Red,
                    fontSize = 70.sp)


            }

        }

    }




private fun DrawScope.drawGame(
    cellSize: Float,
    cellColor:Color,
    borderCallColor:Color,
    gridWidth:Int,
    gridHeight:Int

    ){

    for (i in 0 until gridWidth ){
        for (j in 0 until gridHeight){
            val isBorderCell = i == 0 || j == 0||i == gridWidth -1 || j == gridHeight -1
            drawRect(
                color = if (isBorderCell) borderCallColor
                else if ((i+j) % 2 == 0)cellColor
                else cellColor.copy(alpha = 0.5f),
                topLeft = Offset(x=i* cellSize,y=j*cellSize),
                size = Size(cellSize,cellSize)
            )
        }

    }

}




@Composable
fun Button(onEvent: (SnakeGameEvent) -> Unit
           ,state: SnakeGameState
){
    val bgimage = painterResource(id = R.drawable.bgimage)

    Box(modifier = Modifier.background(Color.Black)) {
        Image(painter = bgimage,
            alpha = 0.7f,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = "background")
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            val customFontFamily = FontFamily(
                Font(R.font.newfont)
            )


            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(200.dp)
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.clickable {
                        onEvent(SnakeGameEvent.RestartGame)
                    },
                    painter =  painterResource(id = R.drawable.red),
                    contentDescription = "Replay",
                    contentScale = ContentScale.Crop
                )
                Text(text = if(state.isGameOver) "Restart" else "New Game",
                    fontSize = 30.sp,
                    color = Color.White,
                    fontFamily = customFontFamily
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(200.dp)
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            when(state.gameState){
                                GameState.IDLE, GameState.PAUSED -> onEvent(SnakeGameEvent.StartGame)
                                GameState.STARTED -> onEvent(SnakeGameEvent.PauseGame)


                            }


                        },

                    painter = painterResource(id = R.drawable.green),
                    contentDescription = "Start",
                    contentScale = ContentScale.Crop
                )
                Text(text = when (state.gameState){
                    GameState.IDLE -> "Start"
                    GameState.STARTED -> "Pause"
                    GameState.PAUSED -> "Resume"

                },
                    fontSize = 30.sp,
                    color = Color.White,
                    fontFamily = customFontFamily
                )

            }
        }
    }

}


private fun DrawScope.drawFood(
    foodImage:ImageBitmap,
    cellSize: Int,
    coordinate: Coordinate
){
    drawImage(
        image = foodImage,
        dstOffset = IntOffset(x = (coordinate.x * cellSize),
            y = (coordinate.y * cellSize)
        ) ,
        dstSize  = IntSize(cellSize,cellSize)
    )

}

private fun DrawScope.drawSnake(
    snakeHeadImage:ImageBitmap,
    cellSize: Float,
    snake: List<Coordinate>,
    snakeback:ImageBitmap
){
    val cellSizeInt = cellSize.toInt()
    snake.forEachIndexed { index, coordinate ->
//        val radius = if (index == snake.lastIndex)cellSize/2.5f else cellSize/2
        if (index==0){
            drawImage(
                image = snakeHeadImage,
                dstOffset = IntOffset(
                    x = (coordinate.x * cellSizeInt),
                    y = (coordinate.y * cellSizeInt)
                ) ,
                dstSize  = IntSize(cellSizeInt,cellSizeInt)
            )

        }else   {
            drawImage(
                image = snakeback,
                dstOffset = IntOffset(
                    x = (coordinate.x * cellSizeInt),
                    y = (coordinate.y * cellSizeInt)
                ) ,
                dstSize  = IntSize(cellSizeInt,cellSizeInt)
            )


//            drawCircle(
//                color = Color.Blue,
//                center = Offset(
//                    x = (coordinate.x * cellSize) + radius,
//                    y = (coordinate.y * cellSize) + radius
//                ),
//                radius = radius
//
//            )

        }

    }
}