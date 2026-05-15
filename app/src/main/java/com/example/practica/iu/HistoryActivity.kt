package com.example.practica.iu

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica.R
import com.example.practica.data.GameRecord
import com.example.practica.ui.theme.*
import com.example.practica.viewmodel.AppViewModelProvider
import com.example.practica.viewmodel.GameHistoryViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PRACTICATheme(darkTheme = true) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
                    HistoryScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HistoryScreen(
    historyViewModel: GameHistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current

    val gameList by historyViewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val customDirective = calculatePaneScaffoldDirective(adaptiveInfo).let { directive ->
        if (configuration.smallestScreenWidthDp < 600) {
            directive.copy(maxHorizontalPartitions = 1)
        } else {
            directive
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<GameRecord>(
        scaffoldDirective = customDirective
    )

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.15f),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().height(70.dp).background(CardBg).padding(horizontal = 16.dp)) {
                Button(
                    onClick = {
                        if (navigator.canNavigateBack()) {
                            scope.launch { navigator.navigateBack() }
                        } else {
                            activity?.finish()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = stringResource(R.string.ButtonGoBack),
                        color = NeonCyan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = stringResource(R.string.ButtonBBDD),
                    modifier = Modifier.align(Alignment.Center),
                    color = WhiteText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    AccessBD(
                        gameList = gameList,
                        onItemClick = { record ->
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, record)
                            }
                        }
                    )
                },
                detailPane = {
                    val selectedRecord = navigator.currentDestination?.contentKey
                    DetailReg(record = selectedRecord)
                }
            )
        }
    }
}

@Composable
fun AccessBD(gameList: List<GameRecord>, onItemClick: (GameRecord) -> Unit) {
    if (gameList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.ButtonNoGame),
                color = WhiteText, fontSize = 18.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(gameList) { record ->
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(CardBg).clickable { onItemClick(record) }.padding(16.dp)
                ) {
                    Text(
                        text = "${record.alias} -- ${record.date}",
                        color = WhiteText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = record.result,
                        color = if(record.result.contains("GUANYAT")) NeonCyan else NeonRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DetailReg(record: GameRecord?) {
    if (record == null) {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.ButtonSelectGame),
                color = Color.Gray,
                fontSize = 18.sp
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(12.dp)).background(CardBg).padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.ButtonDetails),
                color = ElectricBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Alias: ${record.alias}",
                color = WhiteText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Data: ${record.date}",
                color = WhiteText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Mida graella: ${record.columns} x ${record.columns}",
                color = WhiteText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Dificultat: ${record.difficulty}",
                color = WhiteText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Temps restant (segons): ${record.timeLeft}",
                color = WhiteText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Resultat: ${record.result}",
                color = if(record.result.contains("GUANYAT")) NeonCyan else NeonRed,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}