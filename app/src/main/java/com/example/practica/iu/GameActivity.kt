package com.example.practica.iu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.res.Configuration
import kotlinx.coroutines.delay
import com.example.practica.R
import com.example.practica.model.AppConstants
import com.example.practica.model.Player
import com.example.practica.ui.theme.*
import com.example.practica.viewmodel.GameStatus
import com.example.practica.viewmodel.GameViewModel

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alias = intent.getStringExtra(AppConstants.ALIAS) ?: "Jugador"
        val columns = intent.getIntExtra(AppConstants.COLUMNS, 7)
        val time = intent.getBooleanExtra(AppConstants.TIME, false)
        val difficulty = intent.getStringExtra(AppConstants.DIFFICULTY) ?: "Fàcil"

        setContent {
            PRACTICATheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
                    GameScreen(alias, columns, time, difficulty)
                }
            }
        }
    }
}

@Composable
fun GameScreen(alias: String, columns: Int, time: Boolean, difficulty: String, gameViewModel: GameViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        if (!gameViewModel.isBoardCreated) {
            gameViewModel.startGame(columns, time, difficulty)
        }
    }

    LaunchedEffect(gameViewModel.columnFullTrigger) {
        if (gameViewModel.columnFullTrigger > 0) {
            Toast.makeText(context, context.getString(R.string.columnFull), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(gameViewModel.status) {
        if (gameViewModel.status != GameStatus.PLAYING) {
            delay(1000L)
            val intent = Intent(context, ResultsActivity::class.java)
            intent.putExtra(AppConstants.ALIAS, alias)
            intent.putExtra(AppConstants.COLUMNS, columns)
            intent.putExtra(AppConstants.TIME_LEFT, gameViewModel.timeLeft)
            intent.putExtra(AppConstants.RESULT, gameViewModel.finalResultText)
            intent.putExtra(AppConstants.DIFFICULTY, difficulty)
            context.startActivity(intent)
            activity?.finish()
        }
    }

    val onDrop: (Int) -> Unit = { col -> gameViewModel.drop(col) }
    val isHumanTurn = gameViewModel.status == GameStatus.PLAYING && gameViewModel.currentTurn == Player.HUMAN
    val timeColor = if (gameViewModel.isTimeEnabled) NeonRed else ElectricBlue

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.25f),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            GameLandscapeLayout(
                alias = alias,
                columns = columns,
                statusText = gameViewModel.statusText,
                timeText = gameViewModel.timeText,
                timeColor = timeColor,
                isBoardCreated = gameViewModel.isBoardCreated,
                isHumanTurn = isHumanTurn,
                getCell = { row, col -> gameViewModel.board.grid[row][col] },
                onDrop = onDrop
            )
        } else {
            GamePortraitLayout(
                alias = alias,
                columns = columns,
                statusText = gameViewModel.statusText,
                timeText = gameViewModel.timeText,
                timeColor = timeColor,
                isBoardCreated = gameViewModel.isBoardCreated,
                isHumanTurn = isHumanTurn,
                getCell = { row, col -> gameViewModel.board.grid[row][col] },
                onDrop = onDrop
            )
        }
    }
}

@Composable
fun GameHeader(alias: String, isLandscape: Boolean) {
    val height = if (isLandscape) 45.dp else 100.dp
    val fontSize = if (isLandscape) 17.sp else 23.sp

    Row(
        modifier = Modifier.fillMaxWidth().height(height).background(CardBg).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.playerLabel, alias),
            color = WhiteText,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
    }
}

@Composable
fun GameInfoPanel(statusText: String, timeText: String, timeColor: Color, isLandscape: Boolean) {
    val gameSize = if (isLandscape) 14.sp else 20.sp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = statusText,
            fontSize = gameSize,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            color = WhiteText
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = timeText,
            fontSize = gameSize,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            color = timeColor
        )
    }
}

@Composable
fun BoardGame(
    columns: Int,
    isHumanTurn: Boolean,
    getCell: (row: Int, col: Int) -> Player,
    onDrop: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(8.dp).clip(RoundedCornerShape(16.dp)).background(CardBg).border(2.dp, color = Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp)).padding(8.dp)) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (col in 0 until columns) {
                Column(
                    modifier = Modifier.weight(1f).clickable(enabled = isHumanTurn) { onDrop(col) }
                ) {
                    for (row in 0 until columns) {
                        val cellPlayer = getCell(row, col)

                        Box(modifier = Modifier.fillMaxWidth().padding(4.dp).aspectRatio(1f).clip(CircleShape).background(Color.Black.copy(alpha = 0.5f)).border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = cellPlayer != Player.NONE,
                                enter = slideInVertically(
                                    initialOffsetY = { fullHeight -> -fullHeight * (row + 3) },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                                )
                            ) {
                                val tokenColor = if (cellPlayer == Player.HUMAN) NeonRed else NeonYellow
                                Box(modifier = Modifier.fillMaxSize().padding(2.dp).clip(CircleShape).background(tokenColor.copy(alpha = 0.7f)).border(1.dp, tokenColor, CircleShape))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GamePortraitLayout(
    alias: String, columns: Int, statusText: String, timeText: String, timeColor: Color,
    isBoardCreated: Boolean, isHumanTurn: Boolean,
    getCell: (Int, Int) -> Player, onDrop: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeader(alias = alias, isLandscape = false)

        Spacer(modifier = Modifier.height(25.dp))

        GameInfoPanel(statusText, timeText, timeColor, isLandscape = false)

        Spacer(modifier = Modifier.height(40.dp))

        if (isBoardCreated) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                BoardGame(columns, isHumanTurn, getCell, onDrop)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(CardBg))
    }
}

@Composable
fun GameLandscapeLayout(
    alias: String, columns: Int, statusText: String, timeText: String, timeColor: Color,
    isBoardCreated: Boolean, isHumanTurn: Boolean,
    getCell: (Int, Int) -> Player, onDrop: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeader(alias = alias, isLandscape = true)

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameInfoPanel(statusText, timeText, timeColor, isLandscape = true)

            Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                if (isBoardCreated) {
                    Box(modifier = Modifier.fillMaxHeight().aspectRatio(1f)) {
                        BoardGame(columns, isHumanTurn, getCell, onDrop)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Game Vertical", showSystemUi = true)
@Composable
fun GameActivityPreviewVertical() {
    PRACTICATheme(darkTheme = true) {
        GameScreen(alias = "Bruno", columns = 7, time = false, difficulty = "Fàcil")
    }
}

@Preview(showBackground = true, name = "Game Horizontal", device = "spec:parent=pixel_5,orientation=landscape", showSystemUi = true)
@Composable
fun GameActivityPreviewHorizontal() {
    PRACTICATheme(darkTheme = true) {
        GameScreen(alias = "Bruno", columns = 7, time = false, difficulty = "Fàcil")
    }
}