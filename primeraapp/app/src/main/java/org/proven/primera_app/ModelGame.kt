package org.proven.primera_app

import kotlin.random.Random

enum class GameState { WAITING, PLAYING, WON, LOST }

// Marcador
class Player {
    var wins = 0
    var loses = 0
}

//MODEL: solo lógica y datos del juego. No conoce Views ni Activities.
class ModelGame(val numCubes: Int) {

    var positionBall = 0
        private set
    var state = GameState.WAITING
        private set
    val player = Player()

    //Empieza una partida: coloca la bola al azar y pasa a PLAYING.
    fun initGame() {
        positionBall = Random.nextInt(numCubes)
        state = GameState.PLAYING
    }


    //Comprueba la tirada del jugador. true si ha acertado. Si no se está jugando, no hace nada y devuelve false.
    fun checkTiradaJugador(cube: Int): Boolean {
        if (state != GameState.PLAYING) return false
        return if (cube == positionBall) {
            player.wins++
            state = GameState.WON
            true
        } else {
            player.loses++
            state = GameState.LOST
            false
        }
    }
}
