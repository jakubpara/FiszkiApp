package com.example.fiszki.presentation.screens.PracticeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fiszki.R
import com.example.fiszki.presentation.screens.settingsScreen.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: PracticeScreenViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    val settingsState = settingsViewModel.settingsState.collectAsState()
    val currentSettingsState = settingsState.value

    when (currentState) {
        PracticeState.Initial -> {
        }

        PracticeState.EmptyListOrRecordsOver -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Koniec fiszek albo ich brak",
                    fontSize = 24.sp
                )
                Text(
                    text = "End of cards or lack of them",
                    fontSize = 24.sp
                )
            }
        }

        is PracticeState.Practicing -> {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.practice)) },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.clickable(onClick = onBackClick),
                                painter = painterResource(R.drawable.arrow_back_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                contentDescription = stringResource(R.string.go_back),
                            )
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "native language first")
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
                            .padding(24.dp)
                            .height(300.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                shape = RoundedCornerShape(16.dp),
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.TopStart),
                            onClick = {
                                viewModel.processCommand(PracticeCommand.DeleteCardClick)
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.delete_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                        Text(
                            text = currentState.question,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp
                        )
                    }


                    currentState.answers.forEachIndexed { index, answer ->
                        Button(
                            onClick = {
                                val isCorrect = if (index == currentState.correctIndex){true}else{false}
                                viewModel.processCommand(
                                    PracticeCommand.OnAnswerClick(
                                        option = index,
                                        correct = isCorrect
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .height(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(text = answer, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}