package com.example.practica.iu

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica.R
import com.example.practica.ui.theme.*

class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
                    HelpScreen()
                }
            }
        }
    }
}

@Composable
fun HelpScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val onMenuClick: () -> Unit = {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
        activity?.finish()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.help),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.25f),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            HelpLandscapeLayout(onMenuClick)
        } else {
            HelpPortraitLayout(onMenuClick)
        }
    }
}

@Composable
fun HelpHeader(isLandscape: Boolean) {
    val headerHeight = if (isLandscape) 60.dp else 100.dp
    val titleSize = if (isLandscape) 22.sp else 27.sp
    val iconSize = if (isLandscape) 25.dp else 35.dp

    Row(
        modifier = Modifier.fillMaxWidth().height(headerHeight).background(CardBg).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.helper),
            contentDescription = "Ajuda",
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = stringResource(R.string.help_title),
            fontSize = titleSize,
            fontWeight = FontWeight.Black,
            color = WhiteText,
            letterSpacing = 2.sp,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
}

@Composable
fun HelpButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier.clip(CircleShape).background(Brush.horizontalGradient(listOf(ButtonGray, ButtonGrayDark))),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        contentPadding = PaddingValues()
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = WhiteText,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun HelpPortraitLayout(onMenuClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HelpHeader(isLandscape = false)

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.weight(1f).padding(horizontal = 24.dp).verticalScroll(rememberScrollState())) {
            Text(
                text = stringResource(R.string.Instructions),
                fontSize = 18.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Justify,
                color = WhiteText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HelpButton(
                text = stringResource(R.string.ButtonMenu),
                onClick = onMenuClick,
                modifier = Modifier.width(240.dp).height(55.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(CardBg))
        }
    }
}

@Composable
fun HelpLandscapeLayout(onMenuClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        HelpHeader(isLandscape = true)

        Box(modifier = Modifier.weight(1f).padding(horizontal = 24.dp).verticalScroll(rememberScrollState())) {
            Text(
                text = stringResource(R.string.Instructions),
                fontSize = 15.sp,
                lineHeight = 23.sp,
                textAlign = TextAlign.Justify,
                color = WhiteText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            HelpButton(
                text = stringResource(R.string.ButtonMenu),
                onClick = onMenuClick,
                modifier = Modifier.width(220.dp).height(45.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}