package com.example.practica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.example.practica.ui.theme.PRACTICATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0F0F0)
                ) {
                    MenuPrincipalScreen()
                }
            }
        }
    }
}

@Composable
fun MenuPrincipalScreen(mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(75.dp).background(Color(0xFF4CAF40)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🎮",
                fontSize = 30.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Text(
                text = mainViewModel.screenSubtitle,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E0E0),
                contentColor = Color.Black
            )

            Button(
                onClick = {
                    val intent = Intent(context, HelpActivity::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                colors = buttonColors,
                modifier = Modifier.width(240.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = stringResource(R.string.Button1), fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val intent = Intent(context, ConfigurationActivity::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                colors = buttonColors,
                modifier = Modifier.width(240.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = stringResource(R.string.Button2), fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { activity?.finish() },
                colors = buttonColors,
                modifier = Modifier.width(240.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = stringResource(R.string.Button3), fontSize = 18.sp)
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(60.dp).background(Color.Transparent))
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPrincipalPreview() {
    PRACTICATheme(darkTheme = false) {
        MenuPrincipalScreen()
    }
}