package com.example.practica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.key
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import com.example.practica.ui.theme.PRACTICATheme

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alias = intent.getStringExtra("ALIAS") ?: "Jugador"
        val columns = intent.getIntExtra("COLUMNS", 7)
        val time = intent.getBooleanExtra("TIME", false)

        setContent {
            PRACTICATheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color(0xFFF0F0F0)
                ) {
                    GameScreen(alias, columns, time)
                }
            }
        }
    }
}

@Composable
fun GameScreen(alias: String, columns: Int, time: Boolean, gameViewModel: GameViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        if (!gameViewModel.isBoardCreated) {
            gameViewModel.startGame(columns, time)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(80.dp).background(Color(0xFF2196F7)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Jugador: $alias",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val columnFullTrigger = gameViewModel.columnFullTrigger
        LaunchedEffect(columnFullTrigger) {
            if (columnFullTrigger > 0) {
                Toast.makeText(context, "Columna plena! Escull una altra.", Toast.LENGTH_SHORT).show()
            }
        }

        val statusText = when (gameViewModel.status) {
            GameStatus.PLAYING -> if (gameViewModel.currentTurn == Player.HUMAN) stringResource(R.string.ButtonPlayer) else stringResource(R.string.ButtonSystem)
            GameStatus.WON -> if (gameViewModel.winner == Player.HUMAN) stringResource(R.string.ButtonWinPLayer) else stringResource(R.string.ButtonWinSystem)
            GameStatus.DRAW -> stringResource(R.string.ButtonDraw)
            GameStatus.TIME_OUT -> stringResource(R.string.ButtonFinishTime)
        }

        val gameStatus = gameViewModel.status
        LaunchedEffect(gameStatus) {
            if (gameStatus != GameStatus.PLAYING) {
                delay(1000L)

                val intent = Intent(context, ResultsActivity::class.java)
                intent.putExtra("ALIAS", alias)
                intent.putExtra("COLUMNS", columns)
                intent.putExtra("TIME-LEFT", gameViewModel.timeLeft)

                val resultadoStr = when {
                    gameViewModel.winner == Player.HUMAN -> "HAS GUANYAT"
                    gameViewModel.winner == Player.SYSTEM -> "GUANYA LA MÀQUINA"
                    gameStatus == GameStatus.DRAW -> "EMPAT"
                    gameStatus == GameStatus.TIME_OUT -> "TEMPS ESGOTAT"
                    else -> "ERROR"
                }
                intent.putExtra("RESULT", resultadoStr)
                context.startActivity(intent)
                activity?.finish()
            }
        }

        Text(
            text = statusText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(25.dp))

        val timeText = if (gameViewModel.isTimeEnabled) {
            "${gameViewModel.timeLeft} segons."
        } else {
            "No hi ha temps limit."
        }

        val timeColor = if (gameViewModel.isTimeEnabled) {
            Color.Red
        } else {
            Color.Blue
        }

        Text(
            text = timeText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = timeColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (gameViewModel.isBoardCreated) {
            BoardView(gameViewModel = gameViewModel, columns = columns)
        }

        Spacer(modifier = Modifier.height(157.dp))

        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
    }
}

@Composable
fun BoardView(gameViewModel: GameViewModel, columns: Int) {

    key(gameViewModel.boardUpdateTrigger) {
        Row(
            modifier = Modifier.background(Color.Blue).padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (col in 0 until columns) {
                Column(
                    modifier = Modifier.clickable(
                        enabled = (gameViewModel.status == GameStatus.PLAYING && gameViewModel.currentTurn == Player.HUMAN)
                    ) {
                        gameViewModel.drop(col)
                    }
                ) {
                    for (row in 0 until columns) {
                        val cellPlayer = gameViewModel.board.grid[row][col]
                        val circleColor = when (cellPlayer) {
                            Player.NONE -> Color.White
                            Player.HUMAN -> Color.Red
                            Player.SYSTEM -> Color.Yellow
                        }

                        Box(modifier = Modifier.padding(4.dp).size(45.dp).clip(CircleShape).background(circleColor))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    PRACTICATheme(darkTheme = false) {
        val previewViewModel = remember {
            GameViewModel().apply {
                startGame(columns = 7, false)
                drop(col = 3)
                drop(col = 4)
                drop(col = 3)
            }
        }
        GameScreen(
            alias = "Bruno",
            columns = 7,
            time = false,
            gameViewModel = previewViewModel
        )
    }
}