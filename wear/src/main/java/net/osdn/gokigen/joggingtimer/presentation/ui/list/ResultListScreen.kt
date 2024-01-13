package net.osdn.gokigen.joggingtimer.presentation.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.R

@Composable
fun ResultListScreen(navController: NavHostController) {
    /*
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    TimeText()
                    MainScreen(greetingName = stringResource(id = R.string.app_name))
                }
    */

    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.result_list, "RecordListScreen")
    )
}
