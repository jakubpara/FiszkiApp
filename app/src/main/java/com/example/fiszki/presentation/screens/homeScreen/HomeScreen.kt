package com.example.fiszki.presentation.screens.homeScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fiszki.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onReviewClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onCreateCardClick: () -> Unit,
    onSettingClick:() -> Unit
) {
    Log.d("implicationCheck", "HomeScreen created")

    Scaffold(
        Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

        ) {

            HomeScreenCard( modifier = Modifier.padding(horizontal = 24.dp).weight(1f), text = stringResource(R.string.practice), function = {onPracticeClick()} )
            Spacer(modifier = Modifier.height(16.dp))
            HomeScreenCard( modifier = Modifier.padding(horizontal = 24.dp).weight(1f), text = stringResource(R.string.review), function = {onReviewClick()} )

            Spacer(modifier = Modifier.height(16.dp))

            HomeScreenCard( modifier = Modifier.padding(horizontal = 24.dp).weight(1f), text = stringResource(R.string.create_new_card), function = {onCreateCardClick()} )
            Spacer(modifier = Modifier.height(16.dp))
            HomeScreenCard( modifier = Modifier.padding(horizontal = 24.dp).weight(1f), text = stringResource(R.string.settings), function = {onSettingClick()} )

        }
    }
}

//trzeba potem dopisac weight 1f
@Composable
fun HomeScreenCard (modifier: Modifier, text:String, function: ()-> Unit){
    Box(
        modifier = modifier
            .clickable(onClick = {
                Log.d("implicationCheck", "on practice clicked ")
                function()
            }
            )
            .fillMaxWidth(1f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                shape = RoundedCornerShape(16.dp), width = 4.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            .background(Color(233,125,231)), // czemu to jest różowe?- bo Gaba tak chciała
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}