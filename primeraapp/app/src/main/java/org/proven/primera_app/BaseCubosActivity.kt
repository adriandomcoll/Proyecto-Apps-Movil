package org.proven.primera_app

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

/**
 * CONTROLADOR: conecta el Model con la View.
 * Activity3Cubos y Activity6Cubos solo indican su layout y sus cubiletes.
 */
abstract class BaseCubosActivity : ComponentActivity() {

    protected abstract val layoutId: Int
    protected abstract val cubeIds: IntArray

    private lateinit var model: ModelGame
    private lateinit var ivCubes: List<ImageView>
    private lateinit var btStart: Button
    private lateinit var tvResult: TextView
    private lateinit var tvWins: TextView
    private lateinit var tvLoses: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        model = ModelGame(cubeIds.size)
        initElements()
        initListeners()
        updateView()
    }

    private fun initElements() {
        btStart = findViewById(R.id.btStart)
        tvResult = findViewById(R.id.tvResult)
        tvWins = findViewById(R.id.tvWins)
        tvLoses = findViewById(R.id.tvLoses)
        ivCubes = cubeIds.map { findViewById<ImageView>(it) }
    }

    private fun initListeners() {
        btStart.setOnClickListener {
            model.initGame()
            ivCubes.forEach { it.setImageResource(R.drawable.cubilete) }
            updateView()
        }
        ivCubes.forEachIndexed { index, iv ->
            iv.setOnClickListener {
                if (model.state != GameState.PLAYING) return@setOnClickListener
                val win = model.checkTiradaJugador(index)
                iv.setImageResource(if (win) R.drawable.levantaok else R.drawable.levantafail)
                updateView()
            }
        }
    }

    // Pinta en la View lo que dice el Model.
    private fun updateView() {
        tvWins.text = "Wins: ${model.player.wins}"
        tvLoses.text = "Loses: ${model.player.loses}"
        when (model.state) {
            GameState.WAITING -> {
                btStart.text = "Start the game"
                btStart.visibility = View.VISIBLE
                tvResult.visibility = View.INVISIBLE
            }
            GameState.PLAYING -> {
                btStart.visibility = View.INVISIBLE
                tvResult.visibility = View.INVISIBLE
            }
            GameState.WON -> {
                tvResult.text = "You win!"
                tvResult.visibility = View.VISIBLE
                btStart.text = "Start the game"
                btStart.visibility = View.VISIBLE
            }
            GameState.LOST -> {
                tvResult.text = "¡Has perdido!"
                tvResult.visibility = View.VISIBLE
                btStart.text = "Reintentar"
                btStart.visibility = View.VISIBLE
            }
        }
    }
}
