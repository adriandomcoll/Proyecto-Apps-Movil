package org.proven.primera_app

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.proven.primera_app.ui.theme.PrimeraappTheme

class MainComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimeraappTheme {
                TrileroGame()
            }
        }
    }

    @Composable
    fun TrileroGame() {
        val context = LocalContext.current
        var numCubes by remember { mutableIntStateOf(2) }
        var model by remember { mutableStateOf(ModelGame(numCubes)) }
        var resultText by remember { mutableStateOf("¡Pulsa Start para empezar con $numCubes cubos!") }
        var buttonText by remember { mutableStateOf("Start") }
        var cubeStates by remember { mutableStateOf(List(numCubes) { R.drawable.cubilete }) }

        fun startRound() {
            if (model.state == GameState.LOST) {
                // Si ha perdido y pulsa el botón, vuelve a MainActivity
                (context as? Activity)?.finish()
                return
            }

            model = ModelGame(numCubes)
            model.initGame()
            cubeStates = List(numCubes) { R.drawable.cubilete }
            resultText = "Selecciona un cubilete ($numCubes cubos)"
            buttonText = "Reiniciar ronda"
        }

        fun playCube(index: Int) {
            if (model.state == GameState.PLAYING) {
                val won = model.checkTiradaJugador(index)
                val winPos = model.positionBall

                val newStates = MutableList(numCubes) { R.drawable.cubilete }
                if (won) {
                    newStates[index] = R.drawable.levantaok
                    cubeStates = newStates
                    numCubes++
                    resultText = "¡Has acertado! Siguiente nivel: $numCubes cubos."
                    buttonText = "Siguiente nivel ($numCubes cubos)"
                } else {
                    newStates[index] = R.drawable.levantafail
                    if (winPos in newStates.indices) {
                        newStates[winPos] = R.drawable.levantaok
                    }
                    cubeStates = newStates
                    resultText = "¡Has fallado! Volviendo al menú..."
                    buttonText = "Volver al menú principal"
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.fondo),
                    contentScale = ContentScale.Crop
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Nivel actual: $numCubes cubos",
                    fontSize = 24.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 90.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center
                ) {
                    itemsIndexed(cubeStates) { index, resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = "Cube ${index + 1}",
                            modifier = Modifier
                                .size(90.dp)
                                .padding(4.dp)
                                .clickable { playCube(index) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { startRound() }
                ) {
                    Text(text = buttonText)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = resultText,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewSalutacio() {
        TrileroGame()
    }
}
