package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus

@Composable
fun GraphArea(counterManager: ITimerCounter, modifier: Modifier = Modifier)
{
    // 計測中の経過時間を設定
    val currentTime = remember { mutableLongStateOf(0) }
    currentTime.longValue = counterManager.getPastTime()

    val lapCount = remember { mutableIntStateOf(0) }
    lapCount.intValue = counterManager.getLapTimeCount() - 1

    // 基準ラップタイムの取得
    val refId = remember { mutableIntStateOf(0) }
    refId.intValue = AppSingleton.controller.getReferenceTimerSelection()
    val refLapTimeList = counterManager.getReferenceLapTimeList(refId.intValue)
    val refLapTimeCount = refLapTimeList?.size ?: 0

    val textMeasurer = rememberTextMeasurer()

    BoxWithConstraints(modifier = modifier) {
        // x,y軸の描画エリアを設定する
        val width = with(LocalDensity.current) { maxWidth.toPx() }
        val height = with(LocalDensity.current) { maxHeight.toPx() }
        val areaRect = Rect(left = 0f, top = 0f, right = width, bottom = height)

        val path = Path()

        // ラップタイムの進捗を記録する
        if ((refLapTimeList != null)&&(refLapTimeCount > 1))
        {
            val lapTimeList = counterManager.getLapTimeList()
            if (lapTimeList.size > 1)
            {
                val totalRefLapTime = (refLapTimeList[refLapTimeList.size - 1] - refLapTimeList[0])
                path.moveTo(0.0f, (height / 2.0f))

                lapTimeList.forEachIndexed { index, it ->
                    val percentage = (it - lapTimeList[0]).toFloat() / totalRefLapTime.toFloat()
                    val positionX = (width * percentage)
                    val positionY = height / 2.0f
                    if (percentage <= 1.0f) {
                        path.lineTo(x = positionX, y = height / 2.0f)
                        path.addOval(
                            oval = Rect(
                                left = positionX - 2.0f,
                                right = positionX + 2.0f,
                                top = positionY - 2.0f,
                                bottom = positionY + 2.0f
                            )
                        )
                    }
                }
                if (currentTime.longValue < refLapTimeList[refLapTimeList.size - 1])
                {
                    val currentRatio = if (currentTime.longValue < totalRefLapTime) { (currentTime.longValue.toFloat() / totalRefLapTime.toFloat()) } else { 1.0f }
                    val currentPosition = (width * currentRatio)
                    path.lineTo(x = currentPosition, y = height / 2.0f)
                    path.addOval(oval = Rect(left = currentPosition - 4.0f, right = currentPosition + 4.0f, top =  height / 2.0f - 4.0f, bottom = height / 2.0f + 4.0f))
                }
                path.close()
            }
        }

        // 実際の描画
        Canvas(
            Modifier.fillMaxSize()
        ) { // this: DrawScope
            if (refLapTimeCount > 1)
            {
                // x,y軸を描画する
                drawAxis(
                    area = areaRect,
                    currentTime = currentTime.longValue,
                    refLapTimeCount = refLapTimeCount,
                    refLapTimeList = refLapTimeList,
                    lapCount = lapCount.intValue,
                )
                drawPath(path, Color.White, style = Stroke(width = 2.0f))
            }
            else
            {
                // 基準値が設定されていない場合は、平均ラップタイムを表示する
                val lastLapTime = counterManager.getLastLapTime()
                val averageTime = if (lapCount.intValue <= 0) { 0 } else { (lastLapTime.toFloat() / lapCount.intValue.toFloat()).toLong() }
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
}

// x,y軸を描画する
private fun DrawScope.drawAxis(area: Rect, currentTime: Long, refLapTimeCount: Int, refLapTimeList: List<Long>?, lapCount: Int)
{
    val totalLapTime = if ((refLapTimeList != null)&&(refLapTimeCount >= 2)) { refLapTimeList[refLapTimeList.size - 1] - refLapTimeList[0] } else { 0 }
    val lapTimeOffset = if ((refLapTimeList != null)&&(refLapTimeCount >= 2)) { refLapTimeList[0] } else { 0 }
    val refLapPosition = ArrayList<Float>()
    refLapTimeList?.forEach {
        refLapPosition.add((it - lapTimeOffset) / totalLapTime.toFloat())
    }
    val percentage = if (totalLapTime != 0L) { currentTime.toFloat() / totalLapTime.toFloat() } else { 0.0f }
    val drawRatio = if (percentage > 1.0f) { 1.0f } else { percentage }

    // 進捗率を示す色... 基準タイム超過時は Yellow、それまでは Blue
    val progressColor = if (percentage > 1.0f) { Color.Yellow } else { Color.Blue }

    //Log.v("Graph", "refLapTimeList: refId:${refId} lapCount ${refLapTimeList?.size}")
    //Log.v("Graph", "> currentTime: $currentTime  totalLapTime: $totalLapTime ${percentage * 100.0f}% lapCount: $lapCount")

    // 基準値との進捗を示す (基準値が入っていない場合は表示しない）
    drawRect(
        color = progressColor,
        size = Size(width = (area.right - area.left) * drawRatio, height = (area.bottom - area.top))
    )

    // 外枠を描画
    drawLine(
        color = Color.LightGray,
        start = Offset(area.left, area.top),
        end = Offset(area.left, area.bottom)
    )
    drawLine(
        color = Color.LightGray,
        start = Offset(area.left, area.bottom),
        end = Offset(area.right, area.bottom)
    )
    drawLine(
        color = Color.LightGray,
        start = Offset(area.left, area.top),
        end = Offset(area.right, area.top)
    )
    drawLine(
        color = Color.LightGray,
        start = Offset(area.right, area.top),
        end = Offset(area.right, area.bottom)
    )

/*
    // 中心のライン
    drawLine(
        color = Color.LightGray,
        start = Offset(area.left, (area.bottom - area.top) /2.0f),
        end = Offset(area.right, (area.bottom - area.top) /2.0f)
    )
*/

    // 基準LAPの場所を縦線で描画
    refLapPosition.forEachIndexed { index, fl ->
            drawLine(
                color = if (index == lapCount) { Color.White } else { Color.LightGray },
                strokeWidth = if (index == lapCount) { 4.0f } else { 1.0f },
                start = Offset(area.right * fl, area.top),
                end = Offset(area.right * fl, area.bottom)
            )
    }
}
