package com.example.practica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(90.dp).background(Color(0xFF2196F7)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.configuracion),
                contentDescription = "Configuración",
                modifier = Modifier.size(33.dp)
            )

            Text(
                text = configViewModel.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            
            Text(text = stringResource(R.string.ButtonAlias), color = Color.Black, fontSize = 14.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.persona),
                    contentDescription = "Persona",
                    modifier = Modifier.size(30.dp)
                )
                
                OutlinedTextField(
                    value = configViewModel.alias,
                    onValueChange = { configViewModel.onAliasChange(it) },
                    modifier = Modifier.width(220.dp).padding(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.connect4),
                    contentDescription = "Tablero",
                    modifier = Modifier.size(35.dp)
                )

                Text(text = stringResource(R.string.ButtonBoard),
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
                    Text(text = mida.toString(),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = stringResource(R.string.ButtonTime),
                color = Color.Black,
                fontSize = 14.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.reloj),
                    contentDescription = "Tiempo",
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.width(150.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = stringResource(R.string.ButtonStarted))
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfigurationPreview() {
    PRACTICATheme(darkTheme = false) {
        ConfigurationScreen()
    }
}