package com.justplay.habittracker.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.view.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewHabitScreen(
    onBackClick: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.title_create_new_habit)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.app_name)
                    )
                }
            }
        )
    }) { innerPadding ->
        Greeting(
            name = stringResource(R.string.title_create_new_habit),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@PreviewScreenSizes
@Composable
fun CreateNewHabitScreenPreView(

) {
    CreateNewHabitScreen {}
}