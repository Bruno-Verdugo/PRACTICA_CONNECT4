package com.example.practica

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import com.example.practica.ui.theme.PRACTICATheme

class ConfigurationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0F0F0)
                ) {
                    ConfigurationScreen()
                }
            }
        }
    }
}

@Composable
fun ConfigurationScreen(configViewModel: ConfigurationViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(81.dp).background(Color(0xFF2196F7)).padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.configuration),
                    contentDescription = "Configuració",
                    modifier = Modifier.size(25.dp)
                )

                Text(
                    text = stringResource(R.string.config_title),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 10.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.5f).padding(horizontal = 8.dp).verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(R.string.ButtonAlias),
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "Persona",
                            modifier = Modifier.size(20.dp)
                        )

                        OutlinedTextField(
                            value = configViewModel.alias,
                            onValueChange = { configViewModel.onAliasChange(it) },
                            modifier = Modifier.width(200.dp).height(70.dp).padding(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.connect4),
                            contentDescription = "Tauler",
                            modifier = Modifier.size(35.dp)
                        )

                        Text(
                            text = stringResource(R.string.ButtonBoard),
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 9.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 24.dp)
                    ) {
                        val opcionsGraella = listOf(7, 6, 5)

                        opcionsGraella.forEach { mida ->
                            RadioButton(
                                selected = (configViewModel.columns == mida),
                                onClick = { configViewModel.onColumnsChange(mida) },
                                colors = RadioButtonDefaults.colors(Color(0xFF2196F7))
                            )
                            Text(
                                text = mida.toString(),
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = stringResource(R.string.ButtonTime),
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.watch),
                            contentDescription = "Temps",
                            modifier = Modifier.size(33.dp)
                        )

                        Checkbox(
                            checked = configViewModel.time,
                            onCheckedChange = { configViewModel.onTimeChange(it) },
                            colors = CheckboxDefaults.colors(Color(0xFF2196F7))
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(0.5f).padding(horizontal = 24.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(context, GameActivity::class.java)
                            intent.putExtra("ALIAS", configViewModel.alias)
                            intent.putExtra("COLUMNS", configViewModel.columns)
                            intent.putExtra("TIME", configViewModel.time)
                            context.startActivity(intent)
                            activity?.finish()
                        },
                        colors = ButtonDefaults.buttonColors(Color.LightGray),
                        modifier = Modifier.width(240.dp).height(50.dp),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.ButtonStarted),
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }
                }

            }
            Box(modifier = Modifier.fillMaxWidth().height(60.dp).background(Color(0xFF2196F7)))
        }
    } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)).padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.configuration),
                        contentDescription = "Configuració",
                        modifier = Modifier.size(33.dp)
                    )

                    Text(
                        text = stringResource(R.string.config_title),
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ButtonAlias),
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "Persona",
                            modifier = Modifier.size(30.dp)
                        )

                        OutlinedTextField(
                            value = configViewModel.alias,
                            onValueChange = { configViewModel.onAliasChange(it) },
                            modifier = Modifier.width(240.dp).padding(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.connect4),
                            contentDescription = "Tauler",
                            modifier = Modifier.size(35.dp)
                        )

                        Text(
                            text = stringResource(R.string.ButtonBoard),
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 9.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 24.dp)
                    ) {
                        val opcionsGraella = listOf(7, 6, 5)

                        opcionsGraella.forEach { mida ->
                            RadioButton(
                                selected = (configViewModel.columns == mida),
                                onClick = { configViewModel.onColumnsChange(mida) },
                                colors = RadioButtonDefaults.colors(Color(0xFF2196F7))
                            )
                            Text(
                                text = mida.toString(),
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.ButtonTime),
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.watch),
                            contentDescription = "Temps",
                            modifier = Modifier.size(33.dp)
                        )

                        Checkbox(
                            checked = configViewModel.time,
                            onCheckedChange = { configViewModel.onTimeChange(it) },
                            colors = CheckboxDefaults.colors(Color(0xFF2196F7))
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                val intent = Intent(context, GameActivity::class.java)
                                intent.putExtra("ALIAS", configViewModel.alias)
                                intent.putExtra("COLUMNS", configViewModel.columns)
                                intent.putExtra("TIME", configViewModel.time)
                                context.startActivity(intent)
                                activity?.finish()
                            },
                            colors = ButtonDefaults.buttonColors(Color.LightGray),
                            modifier = Modifier.width(240.dp).height(50.dp),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.ButtonStarted),
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
            }
    }
}