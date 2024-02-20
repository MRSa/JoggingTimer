package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.util.Log
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
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter

@Composable
fun GraphAreaLegacy(counterManager: ITimerCounter, refLapTimeList: List<Long>, modifier: Modifier = Modifier)
{
    // ----- 計測中の経過時間を設定
    val currentTime = remember { mutableLongStateOf(0) }
    currentTime.longValue = counterManager.getPastTime()

    // ----- ラップタイムを取得する
    val lapCount = remember { mutableIntStateOf(0) }
    lapCount.intValue = counterManager.getLapTimeCount() - 1

    // 基準ラップタイムの最大値と、基準ラップタイムリストを求める
    var maxLapTime = 0L
    val referenceLapTimeList = ArrayList<Long>()
    refLapTimeList.forEachIndexed { index, _ ->
        if (index > 0)
        {
            val refLapTime = refLapTimeList[index] - refLapTimeList[index - 1]
            if (refLapTime > maxLapTime)
            {
                maxLapTime = refLapTime
            }
            referenceLapTimeList.add(refLapTime)
        }
    }

    // ----- ラップタイムの最大値を求めるとともに、ラップタイム履歴を作成する
    var offsetLapTime = 0L
    val actualLapTimeList = ArrayList<Long>()
    val recordedLapTimeList = counterManager.getLapTimeList()
    recordedLapTimeList.forEachIndexed { index, it ->
        if (index == 0)
        {
            offsetLapTime = it.lapTime
        }
        else
        {
            val lapTime = recordedLapTimeList[index].lapTime - recordedLapTimeList[index - 1].lapTime
            if (lapTime > maxLapTime)
            {
                maxLapTime = lapTime
            }
            actualLapTimeList.add(lapTime)
        }
    }

    // 現在のカウンター状態によって、現在の時刻をプロットするか決める
    when (counterManager.getCurrentCountStatus()) {
        ICounterStatus.START -> {
            // 実行中 ... プロットする
            actualLapTimeList.add((currentTime.longValue) + offsetLapTime - recordedLapTimeList[recordedLapTimeList.size - 1].lapTime)
        }
        ICounterStatus.LAPTIME -> {
            // 実行中(その2) ... プロットする
            actualLapTimeList.add((currentTime.longValue) + offsetLapTime - recordedLapTimeList[recordedLapTimeList.size - 1].lapTime)
        }
        else -> {
            // カウンタ停止中なので、追加プロットはしない
        }
    }

    // 横幅は、計測ラップ数 or 基準ラップ数の大きい方を採用する
    var widthCount = if (referenceLapTimeList.size >= actualLapTimeList.size) { referenceLapTimeList.size } else { actualLapTimeList.size }

    BoxWithConstraints(modifier = modifier) {
        // 描画エリアサイズを獲得する
        val width = with(LocalDensity.current) { maxWidth.toPx() }
        val height = with(LocalDensity.current) { maxHeight.toPx() }
        val areaRect = Rect(left = 0f, top = 0f, right = width, bottom = height)

        // ----- ゼロでの割り算を抑止する... (ありえないはずだが、、以降の処理について保険)
        if (maxLapTime == 0L)
        {
            maxLapTime = height.toLong()
        }
        if (widthCount < 2)
        {
            widthCount = 1
        }

        // 描画エリアと、ラップタイムカウントから大きさを出す
        val boxWidthUnit = width / widthCount
        val boxHeightUnit = height / (maxLapTime * 1.2f)

        // ----- ラップタイムの進捗を折れ線グラフにする
        val path = Path()
        if (actualLapTimeList.size > 1)
        {
            // 先頭ラップのポジションには移動のみ、線を引かない
            val startPositionX = 0.5f * boxWidthUnit // 0.0f でもよいかも
            val startPositionY = (height - (actualLapTimeList[0].toFloat() * boxHeightUnit))
            path.moveTo(
                startPositionX,
                startPositionY
            )
            actualLapTimeList.forEachIndexed { index, _ ->
                val positionX = (index + 0.5f) * boxWidthUnit
                val positionY = (height - (actualLapTimeList[index].toFloat() * boxHeightUnit))
                Log.v(
                    "GraphAreaLegacy",
                    "[$index] X:$positionX Y:$positionY [${actualLapTimeList[index]} / $maxLapTime]"
                )

                if ((positionY > 0.0f) && (positionY <= height)) {
                    path.lineTo(x = positionX, y = positionY)
                    path.addOval(
                        oval = Rect(
                            left = positionX - 2.0f,
                            right = positionX + 2.0f,
                            top = positionY - 2.0f,
                            bottom = positionY + 2.0f
                        )
                    )
                } else {
                    val overY = if (positionY < 0.0f) {
                        0.0f
                    } else {
                        height
                    }
                    path.lineTo(x = positionX, y = overY)
                }
            }
        }
        else
        {
            if (actualLapTimeList.size == 1)
            {
                // 先頭ポジションの所にマークを表示
                val startPositionX = 0.5f * boxWidthUnit
                val startPositionY = (height - (actualLapTimeList[0].toFloat() * boxHeightUnit))
                path.moveTo(
                    startPositionX,
                    startPositionY
                )
                path.addOval(
                    oval = Rect(
                        left = startPositionX - 2.0f,
                        right = startPositionX + 2.0f,
                        top = startPositionY - 2.0f,
                        bottom = startPositionY + 2.0f
                    )
                )
            }
        }
        path.close()

        // ----- 実際のグラフィック描画部分
        Canvas(
            Modifier.fillMaxSize()
        ) { // this: DrawScope
            // ----- 基準ラップタイム(棒グラフ)を描画
            drawReferenceLap(
                area = areaRect,
                currentTime = currentTime.longValue,
                refLapTimeList = refLapTimeList,
                boxWidthUnit = boxWidthUnit,
                boxHeightUnit = boxHeightUnit,
            )

            // 現在のラップタイム推移(折れ線グラフ)を描画
            drawPath(path, Color.White, style = Stroke(width = 2.0f))
        }
    }
}

private fun DrawScope.drawReferenceLap(area: Rect, currentTime: Long, refLapTimeList: List<Long>, boxWidthUnit: Float, boxHeightUnit: Float)
{
    val height = area.height
    val offsetLapTime = refLapTimeList[0]

    // ----- 進捗率を示す色... 基準タイムを超過した時は Yellow(0xffffde03)、それまでは Blue(0xff0336ff)
    val totalLapTime = refLapTimeList[refLapTimeList.size - 1] - offsetLapTime
    val percentage = if (totalLapTime != 0L) { currentTime.toFloat() / totalLapTime.toFloat() } else { 0.0f }
    val progressColor = if (percentage > 1.0f) { Color(0xffffde03) } else { Color(0xff0336ff) }

    var startX = 0.0f
    var previousY = 0L

    // Log.v("GraphLegacy", "drawReferenceLap() : $maxLapTime $totalLapTime $boxWidthUnit  $boxHeightUnit (Rect: ${area.width} x ${area.height})")

    // 棒グラフの描画
    refLapTimeList.forEachIndexed { index, it ->
        val lapTime = it - offsetLapTime
        if (index == 0)
        {
            previousY = lapTime
        }
        else
        {
            val boxHeight = height - boxHeightUnit * (lapTime - previousY)
            previousY = lapTime
            drawRect(
                color = progressColor,
                topLeft = Offset(boxWidthUnit * (index - 1), boxHeight),
                size = Size(width = boxWidthUnit, height = (height - boxHeight))
            )
            //Log.v("GraphLegacy", "offset:[${boxWidthUnit * (index - 1)} , $boxHeight] size:[$boxWidthUnit , ${(height - boxHeight)}] lap:${it.toFloat()}($offsetLapTime) lapTime:$lapTime")
            startX += boxWidthUnit
        }
    }
}
