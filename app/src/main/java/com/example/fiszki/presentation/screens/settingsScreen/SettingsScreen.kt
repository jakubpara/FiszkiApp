package com.example.fiszki.presentation.screens.settingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.fiszki.R
import com.example.fiszki.presentation.screens.addCardScreen.AddCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsState = settingsViewModel.settingsState.collectAsState()
    val currentSettingsState = settingsState.value


    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
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
            verticalArrangement = Arrangement.spacedBy(32.dp),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!currentSettingsState.installationStatus) {
                    Button(onClick = { settingsViewModel.installTranslation() }, enabled = true) {
                        Text(
                            "Zainstaluj tłumaczenie polsko-angielskie"
                        )
                    }
                } else {
                    Button(onClick = {}, enabled = false) {
                        Text("Tłumaczenie już zainstalowane")
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
            Text(
                text = "delete card After clicking 5 times \"i know it\""
            )
            Switch(
                modifier = Modifier.padding(start = 16.dp),
                checked = currentSettingsState.deleteAfterFive,
                onCheckedChange = {
                    settingsViewModel.deleteAfterFive(it)
                }
            )

        }
    }
}
}