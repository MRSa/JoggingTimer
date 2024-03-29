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
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter

@Composable
fun GraphArea(counterManager: ITimerCounter, refLapTimeList: List<Long>, modifier: Modifier = Modifier)
{
    // ----- 計測中の経過時間を設定
    val currentTime = remember { mutableLongStateOf(0) }
    currentTime.longValue = counterManager.getPastTime()

    // ----- ラップタイムを取得する
    val lapCount = remember { mutableIntStateOf(0) }
    lapCount.intValue = counterManager.getLapTimeCount() - 1

    // ----- グラフ描画メイン
    BoxWithConstraints(modifier = modifier) {
        // ----- x,y軸の描画エリアを設定する
        val width = with(LocalDensity.current) { maxWidth.toPx() }
        val height = with(LocalDensity.current) { maxHeight.toPx() }
        val areaRect = Rect(left = 0f, top = 0f, right = width, bottom = height)
        val path = Path()

        // ----- ラップタイムの進捗を記録する
        val lapTimeList = counterManager.getLapTimeList()
        if (lapTimeList.size > 1) {
            val totalRefLapTime = (refLapTimeList[refLapTimeList.size - 1] - refLapTimeList[0])
            path.moveTo(0.0f, (height / 2.0f))

            lapTimeList.forEachIndexed { index, it ->
                val percentage =
                    (it.lapTime - lapTimeList[0].lapTime).toFloat() / totalRefLapTime.toFloat()
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
                val currentRatio = if (currentTime.longValue < totalRefLapTime) {
                    (currentTime.longValue.toFloat() / totalRefLapTime.toFloat())
                } else {
                    1.0f
                }
                val currentPosition = (width * currentRatio)
                path.lineTo(x = currentPosition, y = height / 2.0f)
                path.addOval(
                    oval = Rect(
                        left = currentPosition - 4.0f,
                        right = currentPosition + 4.0f,
                        top = height / 2.0f - 4.0f,
                        bottom = height / 2.0f + 4.0f
                    )
                )
            }
            path.close()
        }

        // ----- 実際の描画部分
        Canvas(
            Modifier.fillMaxSize()
        ) { // this: DrawScope
            // ----- 基準ラップタイムの表示
            drawAxis(
                area = areaRect,
                currentTime = currentTime.longValue,
                refLapTimeList = refLapTimeList,
                lapCount = lapCount.intValue,
            )

            // ----- ラップタイム進捗の表示
            drawPath(path, Color.White, style = Stroke(width = 2.0f))
        }
    }
}

// ---- x,y軸を描画する
private fun DrawScope.drawAxis(area: Rect, currentTime: Long, refLapTimeList: List<Long>, lapCount: Int)
{
    val refLapTimeCount = refLapTimeList.size

    val totalLapTime = if (refLapTimeCount >= 2) { refLapTimeList[refLapTimeList.size - 1] - refLapTimeList[0] } else { 0 }
    val lapTimeOffset = if (refLapTimeCount >= 2) { refLapTimeList[0] } else { 0 }
    val refLapPosition = ArrayList<Float>()
    refLapTimeList.forEach {
        refLapPosition.add((it - lapTimeOffset) / totalLapTime.toFloat())
    }
    val percentage = if (totalLapTime != 0L) { currentTime.toFloat() / totalLapTime.toFloat() } else { 0.0f }
    val drawRatio = if (percentage > 1.0f) { 1.0f } else { percentage }

    // ----- 進捗率を示す色... 基準タイムを超過した時は Yellow(0xffffde03)、それまでは Blue(0xff0336ff)
    val progressColor = if (percentage > 1.0f) { Color(0xffffde03) } else { Color(0xff0336ff) }

    //Log.v("GraphArea", "refLapTimeList: refId:${refId} lapCount ${refLapTimeList?.size}")
    //Log.v("GraphArea", "> currentTime: $currentTime  totalLapTime: $totalLapTime ${percentage * 100.0f}% lapCount: $lapCount")

    // ----- 基準値との進捗を示す (基準値が入っていない場合は表示しない）
    drawRect(
        color = progressColor,
        size = Size(width = (area.right - area.left) * drawRatio, height = (area.bottom - area.top))
    )

    // ---- 外枠を描画 (開始ライン と 終了ライン)
    drawLine(
        color = Color(0xffaaaaaa),
        start = Offset(area.left, area.top),
        end = Offset(area.left, area.bottom)
    )
    drawLine(
        color = Color(0xffaaaaaa),
        start = Offset(area.right, area.top),
        end = Offset(area.right, area.bottom)
    )

/*
    drawLine(
        color = Color(0xffaaaaaa),
        start = Offset(area.left, area.bottom),
        end = Offset(area.right, area.bottom)
    )
    drawLine(
        color = Color(0xffaaaaaa),
        start = Offset(area.left, area.top),
        end = Offset(area.right, area.top)
    )

    // まんなか中心のライン
    drawLine(
        color = Color.LightGray,
        start = Offset(area.left, (area.bottom - area.top) /2.0f),
        end = Offset(area.right, (area.bottom - area.top) /2.0f)
    )
*/

    // ----- 基準LAPの場所を縦線で描画
    refLapPosition.forEachIndexed { index, fl ->
            drawLine(
                color = if (index == lapCount) { Color.White } else { Color(0xffaaaaaa) },
                strokeWidth = if (index == lapCount) { 2.0f } else { 1.0f },
                start = Offset(area.right * fl, area.top),
                end = Offset(area.right * fl, area.bottom)
            )
    }
}
