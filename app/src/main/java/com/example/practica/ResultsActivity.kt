package com.example.practica

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica.ui.theme.PRACTICATheme

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alias = intent.getStringExtra("ALIAS") ?: "Desconegut"
        val columns = intent.getIntExtra("COLUMNS", 7)
        val timeLeft = intent.getIntExtra("TIME-LEFT", 0)
        val result = intent.getStringExtra("RESULT") ?: ""

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
fun ResultsScreen(alias: String, columns: Int, timeLeft: Int, result: String, resultsViewModel: ResultsViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        resultsViewModel.initData(alias, columns, timeLeft, result)
        focusRequester.requestFocus()
    }

    if (isLandscape) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(80.dp).background(Color(0xFF2196F7)).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.ButtonGameResults),
                    modifier = Modifier.padding(15.dp),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(0.4f).padding(horizontal = 8.dp).verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = resultsViewModel.dateText,
                        onValueChange = { resultsViewModel.dateText = it },
                        label = { Text(stringResource(R.string.ButtonDayAndHour)) },
                        modifier = Modifier.fillMaxWidth().height(80.dp).padding(10.dp)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        value = resultsViewModel.logText,
                        onValueChange = { resultsViewModel.logText = it },
                        label = { Text(stringResource(R.string.ButtonLogs)) },
                        modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    OutlinedTextField(
                        value = resultsViewModel.emailText,
                        onValueChange = { resultsViewModel.emailText = it },
                        label = { Text(stringResource(R.string.ButtonDestinationMail)) },
                        modifier = Modifier.fillMaxWidth().height(80.dp).padding(10.dp).focusRequester(focusRequester),
                        singleLine = true
                    )
                }

                Column(
                    modifier = Modifier.weight(0.5f).padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:")
                            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resultsViewModel.emailText))
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Log - ${resultsViewModel.dateText}")
                            intent.putExtra(Intent.EXTRA_TEXT, resultsViewModel.logText)
                            context.startActivity(Intent.createChooser(intent, "Enviar correu..."))
                        },
                        modifier = Modifier.width(220.dp),
                        colors = ButtonDefaults.buttonColors(Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "E-mail",
                            modifier = Modifier.size(20.dp).padding(end = 5.dp)
                        )

                        Text(stringResource(R.string.ButtonSendMail),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val intent = Intent(context, ConfigurationActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            context.startActivity(intent)
                            activity?.finish()
                        },
                        modifier = Modifier.width(220.dp),
                        colors = ButtonDefaults.buttonColors(Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Jugar de nou",
                            modifier = Modifier.size(20.dp)
                        )

                        Text(stringResource(R.string.ButtonNewGame),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            activity?.finishAffinity()
                        },
                        modifier = Modifier.width(220.dp),
                        colors = ButtonDefaults.buttonColors(Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.exit),
                            contentDescription = "Sortir",
                            modifier = Modifier.size(20.dp).padding(end = 5.dp)
                        )

                        Text(stringResource(R.string.ButtonExit),
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Box(modifier = Modifier.fillMaxWidth().height(70.dp).background(Color(0xFF2196F7)))
        }
    } else {
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
                value = resultsViewModel.dateText,
                onValueChange = { resultsViewModel.dateText = it },
                label = { Text(stringResource(R.string.ButtonDayAndHour)) },
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = resultsViewModel.logText,
                onValueChange = { resultsViewModel.logText = it },
                label = { Text(stringResource(R.string.ButtonLogs)) },
                modifier = Modifier.fillMaxWidth().height(120.dp).padding(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = resultsViewModel.emailText,
                onValueChange = { resultsViewModel.emailText = it },
                label = { Text(stringResource(R.string.ButtonDestinationMail)) },
                modifier = Modifier.fillMaxWidth().padding(10.dp).focusRequester(focusRequester),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:")
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resultsViewModel.emailText))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Log - ${resultsViewModel.dateText}")
                    intent.putExtra(Intent.EXTRA_TEXT, resultsViewModel.logText)
                    context.startActivity(Intent.createChooser(intent, "Enviar correu..."))
                },
                modifier = Modifier.fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "E-mail",
                    modifier = Modifier.size(25.dp).padding(end = 5.dp)
                )

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
                Image(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Jugar de nou",
                    modifier = Modifier.size(25.dp)
                )

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
                Image(
                    painter = painterResource(id = R.drawable.exit),
                    contentDescription = "Sortir",
                    modifier = Modifier.size(25.dp).padding(end = 5.dp)
                )

                Text(stringResource(R.string.ButtonExit),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(78.dp))

            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
        }
    }
}