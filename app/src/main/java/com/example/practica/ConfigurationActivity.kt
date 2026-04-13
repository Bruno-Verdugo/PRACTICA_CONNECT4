package com.example.practica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
        Row(modifier = Modifier.fillMaxWidth().height(90.dp).background(Color(0xFF4CAF50)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "⚙️",
                fontSize = 30.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Text(
                text = "CONFIGURACION",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            
            Text(text = stringResource(R.string.Button5), color = Color(0xFF555555), fontSize = 14.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "👤", fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                OutlinedTextField(
                    value = configViewModel.alias,
                    onValueChange = { configViewModel.onAliasChange(it) },
                    singleLine = true,
                    modifier = Modifier.width(200.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🧮", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                Text(text = stringResource(R.string.Button6), color = Color(0xFF555555), fontSize = 14.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 24.dp)
            ) {
                val opcionsGraella = listOf(7, 6, 5)

                opcionsGraella.forEach { mida ->
                    RadioButton(
                        selected = (configViewModel.columnes == mida),
                        onClick = { configViewModel.onColumnesChange(mida) },
                        colors = RadioButtonDefaults.colors(Color(0xFF4CAF50))
                    )
                    Text(text = mida.toString(), modifier = Modifier.padding(end = 16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = stringResource(R.string.Button7), color = Color(0xFF555555), fontSize = 14.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "⏱️", fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                Checkbox(
                    checked = configViewModel.controlTemps,
                    onCheckedChange = { configViewModel.onControlTempsChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        val intent = Intent(context, GameActivity::class.java)
                        intent.putExtra("ALIAS", configViewModel.alias)
                        intent.putExtra("COLUMNES", configViewModel.columnes)
                        intent.putExtra("CONTROL_TEMPS", configViewModel.controlTemps)
                        context.startActivity(intent)
                        activity?.finish()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.width(150.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(text = stringResource(R.string.Button8))
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(60.dp).background(Color.Transparent))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfigurationPreview() {
    PRACTICATheme(darkTheme = false) {
        ConfigurationScreen()
    }
}