package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.util.Log
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
//  サブのボタンエリアの描画＆制御
//   (JoggingTimer互換のボタン)
///////////////////////////////////////////////////

@Composable
fun BtnSubStop(navController: NavHostController, counterManager: ITimerCounter)
{
    Log.v("BTN", "btnStop")

    // ストップ状態時のボタン
    Row() {
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

        ////////////////////  カウンターのスタート  ////////////////////
        IconButton(
            onClick = { counterManager.start() },
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
fun BtnSubStart(counterManager: ITimerCounter)
{
    Log.v("BTN", "btnStart")

    // スタート状態時のボタン
    Row() {
        ////////////////////  カウンターのストップ  ////////////////////
        IconButton(
            onClick = { counterManager.stop() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop",
                tint = Color.LightGray
            )
        }

        ////////////////////  タイムスタンプ記録  ////////////////////
        IconButton(
            onClick = { counterManager.timeStamp() },
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
fun BtnSubFinished(navController: NavHostController, counterManager: ITimerCounter)
{
    Log.v("BTN", "btnFinish")

    // カウントストップ時のボタン
    Row() {
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
            onClick = { counterManager.reset() },
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
            onClick = { counterManager.start() },
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
