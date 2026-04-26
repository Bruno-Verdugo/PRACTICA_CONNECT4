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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica.R
import com.example.practica.model.AppConstants
import com.example.practica.ui.theme.*
import com.example.practica.viewmodel.ConfigurationViewModel

class ConfigurationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
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

    val onStartGame: () -> Unit = {
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(AppConstants.ALIAS, configViewModel.alias)
        intent.putExtra(AppConstants.COLUMNS, configViewModel.columns)
        intent.putExtra(AppConstants.TIME, configViewModel.time)
        intent.putExtra(AppConstants.DIFFICULTY, configViewModel.difficulty)
        context.startActivity(intent)
        activity?.finish()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.25f),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            ConfigLandscapeLayout(
                alias = configViewModel.alias, onAliasChange = { configViewModel.onAliasChange(it) },
                columns = configViewModel.columns, onColumnsChange = { configViewModel.onColumnsChange(it) },
                time = configViewModel.time, onTimeChange = { configViewModel.onTimeChange(it) },
                difficulty = configViewModel.difficulty, onDifficultyChange = { configViewModel.onDifficultyChange(it) },
                onStartGame = onStartGame
            )
        } else {
            ConfigPortraitLayout(
                alias = configViewModel.alias, onAliasChange = { configViewModel.onAliasChange(it) },
                columns = configViewModel.columns, onColumnsChange = { configViewModel.onColumnsChange(it) },
                time = configViewModel.time, onTimeChange = { configViewModel.onTimeChange(it) },
                difficulty = configViewModel.difficulty, onDifficultyChange = { configViewModel.onDifficultyChange(it) },
                onStartGame = onStartGame
            )
        }
    }
}

@Composable
fun ConfigurationHeader(isLandscape: Boolean) {
    val headerHeight = if (isLandscape) 65.dp else 100.dp
    val titleSize = if (isLandscape) 20.sp else 27.sp
    val iconSize = if (isLandscape) 25.dp else 33.dp

    Row(
        modifier = Modifier.fillMaxWidth().height(headerHeight).background(CardBg).padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.configure),
            contentDescription = "Configuració",
            modifier = Modifier.size(iconSize)
        )

        Text(
            text = stringResource(R.string.config_title),
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
fun ConfigSectionTitle(text: String) {
    Text(
        text = text,
        color = WhiteText,
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ConfigurationButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier.width(240.dp).height(55.dp).clip(CircleShape).background(Brush.horizontalGradient(listOf(ElectricBlue, NeonCyan))),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        contentPadding = PaddingValues()
    ) {
        Text(
            text = stringResource(R.string.ButtonStarted),
            color = WhiteText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ConfigFormContent(
    alias: String, onAliasChange: (String) -> Unit,
    columns: Int, onColumnsChange: (Int) -> Unit,
    time: Boolean, onTimeChange: (Boolean) -> Unit,
    difficulty: String, onDifficultyChange: (String) -> Unit
) {
    ConfigSectionTitle(stringResource(R.string.ButtonAlias))
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
            value = alias,
            onValueChange = onAliasChange,
            modifier = Modifier.width(240.dp).padding(start = 10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = WhiteText, unfocusedTextColor = WhiteText,
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = ElectricBlue, unfocusedBorderColor = Color.Gray
            )
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    ConfigSectionTitle(stringResource(R.string.ButtonBoard))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.connect4),
            contentDescription = "connect 4",
            modifier = Modifier.size(35.dp)
        )

        listOf(7, 6, 5).forEach { mida ->
            RadioButton(
                selected = (columns == mida),
                onClick = { onColumnsChange(mida) },
                colors = RadioButtonDefaults.colors(selectedColor = ElectricBlue, unselectedColor = Color.Gray)
            )
            Text(
                text = mida.toString(),
                color = WhiteText,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    ConfigSectionTitle(stringResource(R.string.ButtonTime))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.watch),
            contentDescription = "Rellotge",
            modifier = Modifier.size(33.dp)
        )

        Checkbox(
            checked = time,
            onCheckedChange = onTimeChange,
            colors = CheckboxDefaults.colors(checkedColor = ElectricBlue, uncheckedColor = Color.Gray, checkmarkColor = WhiteText)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    ConfigSectionTitle(stringResource(R.string.ButtonDifficulty))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val opcionsDificultat = listOf(stringResource(R.string.ButtonEasy), stringResource(R.string.ButtonMedium), stringResource(R.string.ButtonHard))
        opcionsDificultat.forEach { diff ->
            RadioButton(
                selected = (difficulty == diff),
                onClick = { onDifficultyChange(diff) },
                colors = RadioButtonDefaults.colors(selectedColor = ElectricBlue, unselectedColor = Color.Gray)
            )
            Text(
                text = diff,
                color = WhiteText,
                modifier = Modifier.padding(end = 10.dp),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ConfigPortraitLayout(
    alias: String, onAliasChange: (String) -> Unit,
    columns: Int, onColumnsChange: (Int) -> Unit,
    time: Boolean, onTimeChange: (Boolean) -> Unit,
    difficulty: String, onDifficultyChange: (String) -> Unit,
    onStartGame: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ConfigurationHeader(isLandscape = false)

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).weight(1f).verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ConfigFormContent(alias, onAliasChange, columns, onColumnsChange, time, onTimeChange, difficulty, onDifficultyChange)

            Spacer(modifier = Modifier.height(150.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ConfigurationButton(onClick = onStartGame)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(CardBg))
    }
}

@Composable
fun ConfigLandscapeLayout(
    alias: String, onAliasChange: (String) -> Unit,
    columns: Int, onColumnsChange: (Int) -> Unit,
    time: Boolean, onTimeChange: (Boolean) -> Unit,
    difficulty: String, onDifficultyChange: (String) -> Unit,
    onStartGame: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        ConfigurationHeader(isLandscape = true)

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.55f).padding(horizontal = 8.dp).verticalScroll(rememberScrollState())
            ) {
                ConfigFormContent(alias, onAliasChange, columns, onColumnsChange, time, onTimeChange, difficulty, onDifficultyChange)
            }

            Column(
                modifier = Modifier.weight(0.45f).padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ConfigurationButton(onClick = onStartGame)
            }
        }
    }
}