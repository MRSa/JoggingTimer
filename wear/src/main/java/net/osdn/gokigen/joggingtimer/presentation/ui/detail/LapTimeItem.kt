package net.osdn.gokigen.joggingtimer.presentation.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert


@Composable
fun LapTimeItem(navController: NavHostController, lapCount: Int, endTime: Long, startTime: Long)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val lapTime = endTime - startTime
        Text(
            text = "[$lapCount] ${TimeStringConvert.getTimeString(lapTime)}", // (${TimeStringConvert.getDiffTimeString(diffTime)})",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
        )
        Divider(color = Color.Gray, thickness = 1.dp)
    }
}
