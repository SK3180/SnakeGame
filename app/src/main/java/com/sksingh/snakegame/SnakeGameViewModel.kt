package com.sksingh.snakegame

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnakeGameViewModel :ViewModel(){


    private val _state = MutableStateFlow(SnakeGameState())
    val state = _state.asStateFlow()

    fun onEvent(event: SnakeGameEvent){
        when(event){
            SnakeGameEvent.PauseGame -> {
                _state.update { it.copy(gameState = GameState.PAUSED) }
            }
            SnakeGameEvent.RestartGame -> {
                _state.value = SnakeGameState()

            }
            SnakeGameEvent.StartGame -> {
                _state.update { it.copy(gameState = GameState.STARTED) }
                viewModelScope.launch {
                    while (state.value.gameState == GameState.STARTED ){
                        val delayMills = when(state.value.snake.size){
                            in 1..5 -> 150L
                            in 6..10 -> 130L
                            else    -> 120L
                        }
                        delay(delayMills)
                        _state.value = updateGame(state.value)
                    }
                }

            }
            is SnakeGameEvent.UpdateDirection -> {
                updateDirection(event.offset,event.canvasWidth)
            }
        }
    }

    private fun updateDirection(offset: Offset, canvasWidth: Int) {
        if (!state.value.isGameOver){
            val cellSize = canvasWidth / state.value.xAxisGridSize
            val tapX = (offset.x/cellSize).toInt()
            val tapY = (offset.y/cellSize).toInt()
            val head = state.value.snake.first()


            _state.update {
                it.copy(
                    direction = when(state.value.direction){
                        Direction.UP,Direction.DOWN ->{
                            if (tapX<head.x) Direction.LEFT else Direction.RIGHT
                        }
                        Direction.LEFT, Direction.RIGHT -> {
                            if (tapY<head.y) Direction.UP else Direction.DOWN
                        }
                    }
                )
            }
        }
    }

    private fun updateGame(currentgame: SnakeGameState):SnakeGameState{
        if (currentgame.isGameOver){
            return currentgame
        }
        val head = currentgame.snake.first()
        val xAxisGridSize = currentgame.xAxisGridSize
        val yAxisGridSize = currentgame.yAxisGridSize

        // Update movement of snake
        val newHead = when(currentgame.direction){
            Direction.UP -> Coordinate(x = head.x,y = (head.y - 1) )

            Direction.DOWN -> Coordinate(x = head.x,y = (head.y + 1))

            Direction.RIGHT -> Coordinate(x = head.x + 1,y = (head.y))
            Direction.LEFT -> Coordinate(x = head.x - 1,y = (head.y))
        }

        // Check snake collide or not
        if (
            currentgame.snake.contains(newHead) || !isWithinBounds(newHead,xAxisGridSize,yAxisGridSize)
        )
        {
            return currentgame.copy(isGameOver = true)
        }
        // check snake eats food
        var newSnake = mutableListOf(newHead) + currentgame.snake
        val newFood = if (newHead == currentgame.food) SnakeGameState.generates()
        else    currentgame.food


        if (newHead != currentgame.food){
            newSnake = newSnake.toMutableList()
            newSnake.removeAt(newSnake.size- 1)
        }
        return currentgame.copy(snake = newSnake, food = newFood)
    }

    private fun isWithinBounds(
        coordinate: Coordinate,
        xAxisGridSize:Int,
        yAxisGridSize:Int
        ): Boolean{
        return coordinate.x in 1 until  xAxisGridSize - 1
                && coordinate.y in 1 until yAxisGridSize - 1

    }
}