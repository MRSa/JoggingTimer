package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter

///////////////////////////////////////////////////
//  メインのボタンエリアの描画＆制御
//   (JoggingTimer互換のボタン)
///////////////////////////////////////////////////

@Composable
fun BtnStop(navController: NavHostController, timerCounter: ITimerCounter)
{
    // ストップ状態時のボタン
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  記録一覧画面へ遷移  ////////////////////
        IconButton(
            onClick = { navController.navigate("RecordListScreen") },
            enabled = true,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_list_24),
                contentDescription = "List",
                tint = Color.LightGray
            )
        }

        ////////////////////  カウンター表示の切り替え  ////////////////////
        IconButton(
            onClick = { timerCounter.toggleCounterMode() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_swap_vert_24),
                contentDescription = "ChangeScreen",
                tint = Color.LightGray
            )
        }

        ////////////////////  カウンターのスタート  ////////////////////
        IconButton(
            onClick = { timerCounter.start() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "Start",
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun BtnStart(timerCounter: ITimerCounter)
{
    // スタート状態時のボタン
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ////////////////////  カウンターのストップ  ////////////////////
        IconButton(
            onClick = { timerCounter.stop() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop",
                tint = Color.LightGray
            )
        }

        ////////////////////  カウンター表示の切り替え  ////////////////////
        IconButton(
            onClick = { timerCounter.toggleCounterMode() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_swap_vert_24),
                contentDescription = "ChangeScreen",
                tint = Color.LightGray
            )
        }

        ////////////////////  タイムスタンプ記録  ////////////////////
        IconButton(
            onClick = { timerCounter.timeStamp() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_flag_24),
                contentDescription = "Lap",
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun BtnFinished(navController: NavHostController, timerCounter: ITimerCounter)
{
    // カウントストップ時のボタン
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ////////////////////  記録一覧画面へ遷移  ////////////////////
        IconButton(
            onClick = { navController.navigate("RecordListScreen") },
            enabled = true,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_list_24),
                contentDescription = "List",
                tint = Color.LightGray
            )
        }

        ////////////////////  カウンターのリセット  ////////////////////
        IconButton(
            onClick = { timerCounter.reset() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_refresh_24),
                contentDescription = "Reset",
                tint = Color.LightGray
            )
        }

        ////////////////////  カウンターのスタート  ////////////////////
        IconButton(
            onClick = { timerCounter.start() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "Start",
                tint = Color.LightGray
            )
        }
    }
}
