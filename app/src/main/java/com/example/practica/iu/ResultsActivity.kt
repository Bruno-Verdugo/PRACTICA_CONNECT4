package com.example.practica.iu

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica.R
import com.example.practica.model.AppConstants
import com.example.practica.ui.theme.*
import androidx.compose.ui.graphics.SolidColor
import com.example.practica.viewmodel.ResultsViewModel
import com.example.practica.viewmodel.AppViewModelProvider
import com.example.practica.viewmodel.ConfigurationViewModel

class ResultsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alias = intent.getStringExtra(AppConstants.ALIAS) ?: "Desconegut"
        val columns = intent.getIntExtra(AppConstants.COLUMNS, 7)
        val timeLeft = intent.getIntExtra(AppConstants.TIME_LEFT, 0)
        val result = intent.getStringExtra(AppConstants.RESULT) ?: ""
        val difficulty = intent.getStringExtra(AppConstants.DIFFICULTY) ?: "Fàcil"

        setContent {
            PRACTICATheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
                    ResultsScreen(alias, columns, timeLeft, result, difficulty)
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(
    alias: String, columns: Int, timeLeft: Int, result: String, difficulty: String,
    resultsViewModel: ResultsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    configViewModel: ConfigurationViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        resultsViewModel.initData(alias, columns, timeLeft, result, difficulty)
        focusRequester.requestFocus()
    }

    val onSendMailClick: () -> Unit = {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resultsViewModel.emailText))
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.emailSubject, resultsViewModel.dateText))
        intent.putExtra(Intent.EXTRA_TEXT, resultsViewModel.logText)
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.resultsEmail)))
    }

    val onPlayAgainClick: () -> Unit = {

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

    val onExitClick: () -> Unit = { activity?.finishAffinity() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.25f),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            ResultsLandscapeLayout(resultsViewModel, focusRequester, onSendMailClick, onPlayAgainClick, onExitClick, onConfigClick)
        } else {
            ResultsPortraitLayout(resultsViewModel, focusRequester, onSendMailClick, onPlayAgainClick, onExitClick, onConfigClick)
        }
    }
}

@Composable
fun ResultsHeader(isLandscape: Boolean, onConfigClick: () -> Unit) {
    val height = if (isLandscape) 60.dp else 100.dp
    val fontSize = if (isLandscape) 20.sp else 20.sp

    Box(modifier = Modifier.fillMaxWidth().height(height).background(CardBg).padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(R.string.ButtonGameResults),
            modifier = Modifier.align(Alignment.Center),
            color = WhiteText,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            style = MaterialTheme.typography.displayLarge
        )

        IconButton(
            onClick = onConfigClick,
            modifier = Modifier.align(Alignment.CenterEnd).padding(5.dp)
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
fun ResultsTextField(
    value: String, onValueChange: (String) -> Unit, label: String,
    modifier: Modifier = Modifier, singleLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = WhiteText) },
        modifier = modifier,
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = WhiteText, unfocusedTextColor = WhiteText,
            focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = ElectricBlue, unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun ResultsButton(
    text: String, icon: Painter, backgroundBrush: Brush,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier.height(50.dp).clip(CircleShape).background(backgroundBrush),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        contentPadding = PaddingValues()
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp).padding(end = 5.dp)
        )

        Text(text,
            color = WhiteText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ResultsPortraitLayout(
    viewModel: ResultsViewModel, fr: FocusRequester,
    onMail: () -> Unit,
    onPlay: () -> Unit,
    onExit: () -> Unit,
    onConfigClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResultsHeader(isLandscape = false, onConfigClick = onConfigClick)

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            ResultsTextField(
                viewModel.dateText,
                { viewModel.dateText = it },
                stringResource(R.string.ButtonDayAndHour),
                Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResultsTextField(
                viewModel.logText,
                { viewModel.logText = it },
                stringResource(R.string.ButtonLogs),
                Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResultsTextField(
                viewModel.emailText,
                { viewModel.emailText = it },
                stringResource(R.string.ButtonDestinationMail),
                Modifier.fillMaxWidth().padding(horizontal = 24.dp).focusRequester(fr),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(100.dp))

            ResultsButton(
                stringResource(R.string.ButtonSendMail),
                painterResource(R.drawable.email),
                Brush.horizontalGradient(listOf(ButtonGray, ButtonGrayDark)),
                onMail, Modifier.fillMaxWidth(0.65f)
            )

            Spacer(modifier = Modifier.height(15.dp))

            ResultsButton(
                stringResource(R.string.ButtonNewGame),
                painterResource(R.drawable.play),
                Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan)),
                onPlay, Modifier.fillMaxWidth(0.65f)
            )

            Spacer(modifier = Modifier.height(15.dp))

            ResultsButton(
                stringResource(R.string.ButtonExit),
                painterResource(R.drawable.exit),
                SolidColor(NeonRed),
                onExit, Modifier.fillMaxWidth(0.65f)
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(CardBg))
    }
}

@Composable
fun ResultsLandscapeLayout(
    viewModel: ResultsViewModel, fr: FocusRequester,
    onMail: () -> Unit,
    onPlay: () -> Unit,
    onExit: () -> Unit,
    onConfigClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResultsHeader(isLandscape = true, onConfigClick = onConfigClick)

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.45f).padding(8.dp)
            ) {
                ResultsTextField(
                    viewModel.dateText,
                    { viewModel.dateText = it },
                    stringResource(R.string.ButtonDayAndHour),
                    Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ResultsTextField(
                    viewModel.logText,
                    { viewModel.logText = it },
                    stringResource(R.string.ButtonLogs),
                    Modifier.fillMaxWidth().height(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ResultsTextField(
                    viewModel.emailText,
                    { viewModel.emailText = it },
                    stringResource(R.string.ButtonDestinationMail),
                    Modifier.fillMaxWidth().focusRequester(fr),
                    singleLine = true
                )
            }

            Column(
                modifier = Modifier.weight(0.55f).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultsButton(
                    stringResource(R.string.ButtonSendMail),
                    painterResource(R.drawable.email),
                    Brush.horizontalGradient(listOf(ButtonGray, ButtonGrayDark)),
                    onMail, Modifier.width(220.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                ResultsButton(
                    stringResource(R.string.ButtonNewGame),
                    painterResource(R.drawable.play),
                    Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan)),
                    onPlay, Modifier.width(220.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                ResultsButton(
                    stringResource(R.string.ButtonExit),
                    painterResource(R.drawable.exit),
                    SolidColor(NeonRed),
                    onExit, Modifier.width(220.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}