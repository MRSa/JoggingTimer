package net.osdn.gokigen.joggingtimer.presentation.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.R

@Composable
fun DetailRecordTitle(navController: NavHostController, title: String, indexId: Int, lapCount: Int)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 14.sp,
            text = stringResource(id = R.string.result_detail)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontSize = 12.sp,
            text = title
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.secondary,
            fontSize = 10.sp,
            text = "Lap : $lapCount"
        )

        // 制御ボタン群
        DetailControlButtons(navController, indexId)

        Divider(color = Color.Gray, thickness = 1.dp)
    }
}
