package com.example.practica

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica.ui.theme.PRACTICATheme
import java.text.SimpleDateFormat
import java.util.*

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alias = intent.getStringExtra("ALIAS") ?: "Desconegut"
        val columns = intent.getIntExtra("COLUMNS", 7)
        val timeLeft = intent.getIntExtra("TIME-LEFT", 0)
        val result = intent.getStringExtra("RESULT") ?: "ERROR"

        setContent {
            PRACTICATheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0F0F0)
                ) {
                    ResultsScreen(alias, columns, timeLeft, result)
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(alias: String, columns: Int, timeLeft: Int, result: String) {
    val context = LocalContext.current
    val activity = context as? Activity

    val dateFormat = SimpleDateFormat("MMM dd, yyyy h:mm:ss a", Locale.getDefault())
    val currentDate = dateFormat.format(Date())

    val timeControlled = timeLeft > 0 || result == "TEMPS ESGOTAT"
    val timeSpent = if (timeControlled) 45 - timeLeft else 0

    val commonLog = "Alias: $alias\n Mida graella: $columns, Temps Total: $timeSpent segons.\n"
    val specificLog = when (result) {
        "HAS GUANYAT" -> {
            var log = "Has guanyat!"
            if (timeControlled) log += "\nHan sobrat $timeLeft segons!"
            log
        }
        "GUANYA LA MÀQUINA" -> "Ha guanyat la màquina!"
        "EMPAT" -> "Heu empatat!"
        "TEMPS ESGOTAT" -> "Has esgotat el temps!"
        else -> ""
    }
    val finalLog = commonLog + specificLog

    var dateText by remember { mutableStateOf(currentDate) }
    var logText by remember { mutableStateOf(finalLog) }
    var emailText by remember { mutableStateOf("msendin@diei.udl.cat") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.ButtonGameResults),
                modifier = Modifier.padding(37.dp),
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = dateText,
            onValueChange = { dateText = it },
            label = { Text(stringResource(R.string.ButtonDayAndHour)) },
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = logText,
            onValueChange = { logText = it },
            label = { Text(stringResource(R.string.ButtonLogs)) },
            modifier = Modifier.fillMaxWidth().height(120.dp).padding(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text(stringResource(R.string.ButtonDestinationMail)) },
            modifier = Modifier.fillMaxWidth().padding(10.dp).focusRequester(focusRequester),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailText))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Log - $dateText")
                intent.putExtra(Intent.EXTRA_TEXT, logText)
                context.startActivity(Intent.createChooser(intent, "Enviar correu..."))
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(Color.LightGray)
        ) {
            Text(stringResource(R.string.ButtonSendMail),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val intent = Intent(context, ConfigurationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
                activity?.finish()
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(Color.LightGray)
        ) {
            Text(stringResource(R.string.ButtonNewGame),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                activity?.finishAffinity()
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(Color.LightGray)
        ) {
            Text(stringResource(R.string.ButtonExit),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(78.dp))

        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Victoria con Tiempo")
@Composable
fun ResultsScreenWinPreview() {
    PRACTICATheme(darkTheme = false) {
        // Simulamos una victoria donde sobraron 16 segundos
        ResultsScreen(
            alias = "p1",
            columns = 7,
            timeLeft = 16,
            result = "HAS GUANYAT"
        )
    }
}

@Preview(showBackground = true, name = "Empate")
@Composable
fun ResultsScreenDrawPreview() {
    PRACTICATheme(darkTheme = false) {
        // Simulamos un empate en tablero de 6x6
        ResultsScreen(
            alias = "User2",
            columns = 6,
            timeLeft = 0,
            result = "EMPAT"
        )
    }
}

@Preview(showBackground = true, name = "Tiempo Agotado")
@Composable
fun ResultsScreenTimeOutPreview() {
    PRACTICATheme(darkTheme = false) {
        // Simulamos que se acabó el tiempo (timeLeft es 0)
        ResultsScreen(
            alias = "p1",
            columns = 7,
            timeLeft = 0,
            result = "TEMPS ESGOTAT"
        )
    }
}