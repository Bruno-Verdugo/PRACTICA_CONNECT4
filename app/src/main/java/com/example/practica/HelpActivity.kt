package com.example.practica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica.ui.theme.PRACTICATheme

class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF0F0F0)
                ) {
                    HelpScreen()
                }
            }
        }
    }
}

@Composable
fun HelpScreen(helpViewModel: HelpViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(90.dp).background(Color(0xFF4CAF50)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "❓",
                fontSize = 30.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Text(
                text = helpViewModel.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        Box(
            modifier = Modifier.weight(1f).padding(horizontal = 24.dp).verticalScroll(rememberScrollState())
        ) {
            Text(
                text = helpViewModel.instructions,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Justify,
                color = Color(0xFF444444),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    activity?.finish()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0),
                    contentColor = Color.Black
                ),
                modifier = Modifier.width(200.dp).height(50.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(stringResource(R.string.Button4), fontSize = 18.sp)
            }

            Box(modifier = Modifier.fillMaxWidth().height(60.dp).background(Color.Transparent))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HelpScreenPreview() {
    PRACTICATheme(darkTheme = false) {
        HelpScreen()
    }
}