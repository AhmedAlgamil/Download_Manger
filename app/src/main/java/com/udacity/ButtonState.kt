package com.udacity


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()

    fun next() = when (this) {
        Clicked -> Loading
        Loading -> Completed
        Completed -> Clicked
        else -> {}
    }

}