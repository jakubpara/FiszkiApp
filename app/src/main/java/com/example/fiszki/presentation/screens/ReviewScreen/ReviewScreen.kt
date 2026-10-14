package com.example.fiszki.presentation.screens.ReviewScreen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.fiszki.R
import com.example.fiszki.presentation.screens.settingsScreen.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: ReviewScreenViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    val settingsState = settingsViewModel.settingsState.collectAsState()
    val currentSettingsState = settingsState.value



    //zwrócić uwage na linie wywołania bo przez to że brak nawiasów to to jakby nie może działać


    when (currentState) {


        ReviewingState.Finished -> {onBackClick()}
        ReviewingState.Initial -> {
           Text("Pozdrawiam")
        }

        is ReviewingState.SeeFirstPage -> {
            WholeScreen(
                word = currentState.fiszka.input,
                translation = currentState.fiszka.translation,
                onBackClick = onBackClick,
                viewModel = viewModel,
                settingsViewModel = settingsViewModel,
            )
        }


        ReviewingState.EmptyListOrRecordsOver -> {
            Scaffold(
                Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(R.string.quick_checkup))
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.clickable(onClick = onBackClick).padding(start = 16.dp),
                                painter = painterResource(R.drawable.arrow_back_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                contentDescription = stringResource(R.string.go_back),
                            )
                        }
                    )
                }

            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Koniec fiszek albo ich brak",
                        fontSize = 24.sp,
                    )
                    Text(
                        text = "End of cards or lack of them ",
                        fontSize = 24.sp,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WholeScreen(
    word: String,
    translation: String,
    onBackClick: () -> Unit,
    viewModel: ReviewScreenViewModel,
    settingsViewModel: SettingsViewModel,
){

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    val settingsState = settingsViewModel.settingsState.collectAsState()
    val currentSettingsState = settingsState.value

    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 300))

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.quick_checkup), modifier = Modifier)
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(onClick =  onBackClick ).padding(start = 16.dp),
                        painter = painterResource(R.drawable.arrow_back_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                        contentDescription = stringResource(R.string.go_back),
                    )
                }
            )
        }

    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),


            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "native language first"
                )
                Switch(
                    modifier = Modifier.padding(start = 16.dp),
                    checked = currentSettingsState.nativeLanguage,
                    onCheckedChange = {
                        settingsViewModel.updateLanguage(it)
                    }
                )
            }


            Box(
                modifier = Modifier
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .padding(24.dp)
                    .fillMaxWidth(1f)
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        shape = RoundedCornerShape(16.dp), width = 4.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    .background(MaterialTheme.colorScheme.primary)

                    .clickable(onClick = {
                        Log.d("implicationCheck", "on practice clicked ")
                        isFlipped = !isFlipped
                    }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    modifier =if (isFlipped){ Modifier.align(Alignment.TopEnd)}else{Modifier.align(Alignment.TopStart)},
                    onClick = {
                        viewModel.processCommand(ReviewingCommand.DeleteCardClick)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                        contentDescription = stringResource(R.string.delete)
                    )
                }
                Text(



                    text =  if (currentSettingsState.nativeLanguage){
                if (isFlipped)translation else word
            }else{
                if (isFlipped)word else translation
            },
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    modifier = Modifier.graphicsLayer{
                        scaleX = if (rotation in 90f..270f) -1f else 1f}.padding(start = 8.dp, end = 8.dp),

                )
            }
//TODO(tu są hardcoded rzeczy to trzeba zmienić for sure)
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.weight(0.5f)
            ) {
                Button(
                    onClick = {viewModel.processCommand(ReviewingCommand.DontRememberClick)
                              isFlipped = false},
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp, end = 8.dp),
                    shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                        containerColor = Color.Red,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Red,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Nie umiem",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Button(
                    onClick = {viewModel.processCommand(ReviewingCommand.IKnowItClick)
                              isFlipped = false},
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 24.dp),
                    shape = RoundedCornerShape(16.dp), colors = ButtonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Green,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Już umiem",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)


                    )
                }

            }

        }

    }
}