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
fun LapTimeItem(navController: NavHostController, lapCount: Int, data: LapTimeDataItem)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            //text = "[$lapCount] ${TimeStringConvert.getTimeString(data.lapTime)} (${TimeStringConvert.getDiffTimeString(data.diffTime)})",
            text = "[%02d] %s (%s)".format(lapCount, TimeStringConvert.getTimeString(data.lapTime), TimeStringConvert.getDiffTimeString(data.diffTime)),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            color = Color.White
        )
        Divider(color = Color.DarkGray, thickness = 1.dp)
    }
}
