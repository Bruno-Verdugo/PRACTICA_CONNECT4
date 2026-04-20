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
import com.example.practica.ui.theme.PRACTICATheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0F0F0)
                ) {
                    PrincipalScreen()
                }
            }
        }
    }
}

@Composable
fun PrincipalScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)).padding(horizontal = 16.dp)) {

            Column(
                modifier = Modifier.align(Alignment.CenterStart),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mando),
                    contentDescription = "Mando",
                    modifier = Modifier.size(35.dp)
                )
                Text(
                    text = stringResource(R.string.main_gametitle),
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Text(
                text = stringResource(R.string.main_title),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    val intent = Intent(context, HelpActivity::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                modifier = Modifier.width(240.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = stringResource(R.string.ButtonHelp),
                    fontSize = 18.sp,
                    color = Color.Black)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val intent = Intent(context, ConfigurationActivity::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                modifier = Modifier.width(240.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = stringResource(R.string.ButtonStart),
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { activity?.finish() },
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                modifier = Modifier.width(240.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = stringResource(R.string.ButtonExit),
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(id = R.drawable.connect4),
                contentDescription = "Logo Connect 4",
                modifier = Modifier.fillMaxWidth(0.8f).height(180.dp)
            )
        }

        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF2196F7)))
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPrincipalPreview() {
    PRACTICATheme(darkTheme = false) {
        PrincipalScreen()
    }
}