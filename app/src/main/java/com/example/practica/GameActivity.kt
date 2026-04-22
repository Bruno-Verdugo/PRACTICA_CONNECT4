package com.example.practica

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.key
import android.widget.Toast
import androidx.compose.ui.platform.LocalConfiguration
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

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        if (!gameViewModel.isBoardCreated) {
            gameViewModel.startGame(columns, time)
        }
    }

    LaunchedEffect(gameViewModel.columnFullTrigger) {
        if (gameViewModel.columnFullTrigger > 0) {
            Toast.makeText(context,
                "Columna plena! Escull una altra.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(gameViewModel.status) {
        if (gameViewModel.status != GameStatus.PLAYING) {
            delay(1000L)

            val intent = Intent(context, ResultsActivity::class.java)
            intent.putExtra("ALIAS", alias)
            intent.putExtra("COLUMNS", columns)
            intent.putExtra("TIME-LEFT", gameViewModel.timeLeft)
            intent.putExtra("RESULT", gameViewModel.finalResultText)
            context.startActivity(intent)
            activity?.finish()
        }
    }

    if(isLandscape){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(70.dp).background(Color(0xFF2196F7)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Jugador: $alias",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 10.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.4f).padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = gameViewModel.statusText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = gameViewModel.timeText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = gameViewModel.timeColor
                    )
                }

                Column(
                    modifier = Modifier.weight(0.8f).fillMaxHeight().padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (gameViewModel.isBoardCreated) {
                        Box(
                            modifier = Modifier.fillMaxHeight().aspectRatio(1f)
                        ) {
                            BoardView(gameViewModel = gameViewModel, columns = columns)
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Jugador: $alias",
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = gameViewModel.statusText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = gameViewModel.timeText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = gameViewModel.timeColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (gameViewModel.isBoardCreated) {
                BoardView(gameViewModel = gameViewModel, columns = columns)
            }

            Spacer(modifier = Modifier.height(165.dp))

            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
        }
    }
}

@Composable
fun BoardView(gameViewModel: GameViewModel, columns: Int) {

    key(gameViewModel.boardUpdateTrigger) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.Blue).padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (col in 0 until columns) {
                Column(
                    modifier = Modifier.weight(1f).clickable(
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

                        Box(modifier = Modifier.fillMaxWidth().padding(2.dp).aspectRatio(1f).clip(CircleShape).background(circleColor))
                    }
                }
            }
        }
    }
}