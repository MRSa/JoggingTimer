package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@Composable
fun DrawAverageLapTime(counterManager: ITimerCounter, modifier: Modifier = Modifier)
{
    val textMeasurer = rememberTextMeasurer()
    val lapCount = remember { mutableIntStateOf(0) }
    lapCount.intValue = counterManager.getLapTimeCount() - 1

    BoxWithConstraints(modifier = modifier) {
        Canvas(
            Modifier.fillMaxSize()
        ) { // this: DrawScope
            // ---- 基準値が設定されていない場合は、平均ラップタイムを表示する
            val lastLapTime = counterManager.getLastLapTime()
            val averageTime = if (lapCount.intValue <= 0) {
                0
            } else {
                (lastLapTime.toFloat() / lapCount.intValue.toFloat()).toLong()
            }
            val averageString = "  Average: ${TimeStringConvert.getTimeString(averageTime)}"
            val measuredText =
                textMeasurer.measure(
                    AnnotatedString(averageString),
                    constraints = Constraints.fixedWidth((size.width * 2f / 3f).toInt()),
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(fontSize = 14.sp, color = Color.LightGray)
                )
            drawText(measuredText)
        }
    }
}
