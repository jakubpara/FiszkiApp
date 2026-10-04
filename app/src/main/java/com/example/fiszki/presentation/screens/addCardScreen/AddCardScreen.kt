package com.example.fiszki.presentation.screens.addCardScreen

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fiszki.R
import com.example.fiszki.presentation.screens.settingsScreen.SettingsState
import com.example.fiszki.presentation.screens.settingsScreen.SettingsViewModel
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBackClick: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    addCardViewModel: AddCardViewModel = hiltViewModel()
) {


    val state = addCardViewModel.state.collectAsState()
    val currentState = state.value

    val settingsState = settingsViewModel.settingsState.collectAsState()
    val currentSettingsState = settingsState.value


    when (currentState) {
        AddCardState.Finished -> {
            onBackClick()
        }

        is AddCardState.FirstPageInput -> {


            PrototypeScreen(
                Modifier,
                page = "1",
                enabled1 = false,
                enabled2 = false,
                enabled3 = false,
                button1Click = {},
                button2Click = {},
                button3Click = {},
                onBackClick = onBackClick,
                addCardViewModel = addCardViewModel,
                currentState = currentState,
                settingsViewModel = settingsViewModel,
                currentSettingsState = currentSettingsState

            )
        }

        is AddCardState.SecondPageInput -> {
            PrototypeScreen(
                Modifier,
                page = "2",
                enabled1 = true,
                enabled2 = currentState.isEnough,
                enabled3 = currentState.isEnough,
                button1Click = {
                    addCardViewModel.processCommand(AddCardCommand.AcceptSuggestion(currentSettingsState.installationStatus))
                    Log.d("Test translatora", "przed wywołaniem getsettings")
                               settingsViewModel.getSettings()
                    Log.d("Test translatora", "po wywołaniem getsettings")
                             },
                button2Click = { addCardViewModel.processCommand(AddCardCommand.SaveAndCreateNext) },
                button3Click = { addCardViewModel.processCommand(AddCardCommand.SaveFiszka) },
                onBackClick = onBackClick,
                addCardViewModel = addCardViewModel,
                currentState = currentState,
                settingsViewModel = settingsViewModel,
                currentSettingsState = currentSettingsState
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrototypeScreen(
    modifier: Modifier = Modifier,
    page: String, enabled1: Boolean, enabled2: Boolean, enabled3: Boolean,
    button1Click: () -> Unit, button2Click: () -> Unit, button3Click: () -> Unit,
    onBackClick: () -> Unit,
    addCardViewModel: AddCardViewModel,
    currentState: AddCardState,
    settingsViewModel: SettingsViewModel,
    currentSettingsState: SettingsState
) {
    val state = addCardViewModel.state.collectAsState()
    val currentState = state.value

    val settingsState = settingsViewModel.settingsState.collectAsState()
    val currentSettingsState = settingsState.value

    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Card")
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable(onClick = { addCardViewModel.processCommand(
                            AddCardCommand.OnBackClick) } ),
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
                .verticalScroll(rememberScrollState()),


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

                        Log.d(
                            "debugowanko",
                            "onCheckedValueChange ${currentSettingsState.nativeLanguage}: "
                        )
                        settingsViewModel.updateLanguage(it)
                        Log.d(
                            "debugowanko",
                            "onCheckedValueChange2 ${currentSettingsState.nativeLanguage}: "
                        )
                    }
                )
            }

            Box(
                modifier = modifier
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                        scaleX = if (isFlipped == false) {
                            1f
                        } else -1f
                    }
                    .height(400.dp)
                    .padding(24.dp)
                    .fillMaxWidth(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        shape = RoundedCornerShape(16.dp), width = 4.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    .background(MaterialTheme.colorScheme.primary)

                    .clickable(onClick = {
                        Log.d("implicationCheck", "on practice clicked ")
                        if (currentState is AddCardState.FirstPageInput) {
                            if (currentState.isEnough == true) {
                                Log.d("implicationCheck", "kliknieto w pierwszym stanie ")
                                isFlipped = !isFlipped
                                addCardViewModel.processCommand(AddCardCommand.OnCardClick)

                            }
                        }
                        if (currentState is AddCardState.SecondPageInput) {
                            Log.d("implicationCheck", "kliknieto w drugim stanie ")
                            isFlipped = !isFlipped
                            addCardViewModel.processCommand(AddCardCommand.OnCardClick)

                        }
                    }
                    ),
            ) {

                Text(
                    text = page,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .padding(start = 16.dp, top = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = when (currentState) {
                        is AddCardState.FirstPageInput -> if (currentSettingsState.nativeLanguage) currentState.text else currentState.usersTranslation

                        is AddCardState.SecondPageInput -> if (currentSettingsState.nativeLanguage) currentState.usersTranslation else currentState.nativeText
                        else -> ""
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 8.dp),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp
                    ),
                    onValueChange = {
                        if (currentState is AddCardState.FirstPageInput) {
                            if (currentSettingsState.nativeLanguage == true) {
                                addCardViewModel.processCommand(

                                    AddCardCommand.InputText(
                                        nativeText = it,
                                        usersTranslation = currentState.usersTranslation,
                                        isNative = currentSettingsState.nativeLanguage
                                    )
                                )
                            }else{
                                addCardViewModel.processCommand(

                                    AddCardCommand.InputText(
                                        nativeText = currentState.text,
                                        usersTranslation = it,
                                        isNative = currentSettingsState.nativeLanguage
                                    )
                                )
                            }
                        }else if(currentState is AddCardState.SecondPageInput){
                            if (currentSettingsState.nativeLanguage == true) {
                                addCardViewModel.processCommand(

                                    AddCardCommand.InputText(
                                        nativeText = currentState.nativeText,
                                        usersTranslation = it,
                                        isNative = currentSettingsState.nativeLanguage
                                    )
                                )
                            }else{
                                addCardViewModel.processCommand(

                                    AddCardCommand.InputText(
                                        nativeText = it,
                                        usersTranslation = currentState.usersTranslation,
                                        isNative = currentSettingsState.nativeLanguage
                                    )
                                )
                            }
                        }
                    }
                )
            }


            //TODO(tu są hardcoded rzeczy to trzeba zmienić for sure)
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.height(320.dp)
            ) {
                Button(
                    enabled = enabled1,
                    onClick = { button1Click() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = Color.Black
                    )
                ) {
                    if (currentSettingsState.installationStatus == true) { // w zaleznosci czy w data store w settingsrepository pokazuje sie ze pobrane czy nie inna wersja
                        Text(
                            text = "Translate into English",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Text(
                            text = "download automatically translation *requires 30 mb ",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Button(
                    enabled = enabled2,
                    onClick = { button2Click() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "zapisz i idz dalej",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Button(
                    enabled = enabled3,
                    onClick = { button3Click() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp, end = 24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "zapisz i wyjdz ",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

            }

        }

    }
}


