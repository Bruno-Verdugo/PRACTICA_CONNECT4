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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.SolidColor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica.R
import com.example.practica.model.AppConstants
import com.example.practica.ui.theme.*
import com.example.practica.viewmodel.ConfigurationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
                    PrincipalScreen()
                }
            }
        }
    }
}

@Composable
fun PrincipalScreen(configViewModel: ConfigurationViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val onStartClick: () -> Unit = {
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(AppConstants.ALIAS, configViewModel.alias)
        intent.putExtra(AppConstants.COLUMNS, configViewModel.columns)
        intent.putExtra(AppConstants.TIME, configViewModel.time)
        intent.putExtra(AppConstants.DIFFICULTY, configViewModel.difficulty)
        context.startActivity(intent)
        activity?.finish()
    }

    val onConfigClick: () -> Unit = {
        val intent = Intent(context, ConfigurationActivity::class.java)
        context.startActivity(intent)
    }

    val onHelpClick: () -> Unit = {
        val intent = Intent(context, HelpActivity::class.java)
        context.startActivity(intent)
        activity?.finish()
    }

    val onExitClick: () -> Unit = { activity?.finish() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.1f),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            MainLandscapeLayout(onStartClick, onHelpClick, onExitClick, onConfigClick)
        } else {
            MainPortraitLayout(onStartClick, onHelpClick, onExitClick, onConfigClick)
        }
    }
}

@Composable
fun MainHeader(isLandscape: Boolean, onConfigClick: () -> Unit) {
    val header = if (isLandscape) 50.dp else 100.dp
    val titleSize = if (isLandscape) 20.sp else 27.sp

    Box(modifier = Modifier.fillMaxWidth().height(header).background(CardBg).padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.controller),
                contentDescription = "Control",
                modifier = Modifier.size(if (isLandscape) 25.dp else 35.dp)
            )

            Text(
                text = stringResource(R.string.main_gametitle),
                fontSize = if (isLandscape) 11.sp else 14.sp,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource(R.string.main_title),
            modifier = Modifier.align(Alignment.Center),
            fontSize = titleSize,
            fontWeight = FontWeight.Black,
            color = WhiteText,
            letterSpacing = 2.sp,
            style = MaterialTheme.typography.displayLarge
        )

        IconButton(
            onClick = onConfigClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.configure),
                contentDescription = "Configuració",
                modifier = Modifier.size(if (isLandscape) 25.dp else 33.dp)
            )
        }
    }
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    backgroundBrush: Brush,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier.clip(CircleShape).background(backgroundBrush),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        contentPadding = PaddingValues()
    ) {
        if (icon != null) {
            Image(painter = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontSize = 16.sp,
            color = WhiteText,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MainPortraitLayout(onStart: () -> Unit, onHelp: () -> Unit, onExit: () -> Unit, onConfig: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MainHeader(isLandscape = false, onConfigClick = onConfig)

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainButton(
                modifier = Modifier.width(240.dp).height(55.dp),
                text = stringResource(R.string.ButtonHelp),
                icon = painterResource(id = R.drawable.helper),
                backgroundBrush = Brush.horizontalGradient(listOf(ButtonGray, ButtonGrayDark)),
                onClick = onHelp,
            )

            Spacer(modifier = Modifier.height(20.dp))

            MainButton(
                modifier = Modifier.width(240.dp).height(55.dp),
                text = stringResource(R.string.ButtonStart),
                backgroundBrush = Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan)),
                onClick = onStart
            )

            Spacer(modifier = Modifier.height(20.dp))

            MainButton(
                modifier = Modifier.width(240.dp).height(55.dp),
                text = stringResource(R.string.ButtonExit),
                icon = painterResource(id = R.drawable.exit),
                backgroundBrush = SolidColor(NeonRed),
                onClick = onExit
            )

            Spacer(modifier = Modifier.height(100.dp))

            Image(
                modifier = Modifier.fillMaxWidth(0.6f).height(125.dp),
                painter = painterResource(id = R.drawable.icon_connect),
                contentDescription = "Logo"
            )
        }
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(CardBg))
    }
}

@Composable
fun MainLandscapeLayout(onStart: () -> Unit, onHelp: () -> Unit, onExit: () -> Unit, onConfig: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MainHeader(isLandscape = true, onConfigClick = onConfig)

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_connect),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxHeight(0.45f).width(80.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MainButton(
                    Modifier.width(220.dp).height(45.dp),
                    stringResource(R.string.ButtonHelp),
                    painterResource(R.drawable.helper),
                    Brush.horizontalGradient(listOf(ButtonGray, ButtonGrayDark)),
                    onHelp
                )

                Spacer(modifier = Modifier.height(15.dp))

                MainButton(
                    Modifier.width(220.dp).height(45.dp),
                    stringResource(R.string.ButtonStart),
                    null,
                    Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan)),
                    onStart
                )

                Spacer(modifier = Modifier.height(15.dp))

                MainButton(
                    Modifier.width(220.dp).height(45.dp),
                    stringResource(R.string.ButtonExit),
                    painterResource(R.drawable.exit),
                    SolidColor(NeonRed),
                    onExit
                )
            }
            Image(
                painter = painterResource(id = R.drawable.icon_connect),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxHeight(0.45f).width(80.dp)
            )
        }
        Box(modifier = Modifier.fillMaxWidth().height(50.dp).background(CardBg))
    }
}