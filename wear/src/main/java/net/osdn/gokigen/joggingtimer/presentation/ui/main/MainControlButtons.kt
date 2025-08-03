package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter

//////////////////////////////////////////////////////////////////////////////
//  メインのボタンエリアの描画＆制御
//   (JoggingTimer互換のボタン)
//
//  [MEMO] How to handle a long click event on wear compose?
//        (use horologist-compose-material Button)
//    https://stackoverflow.com/questions/78034795/
//////////////////////////////////////////////////////////////////////////////

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun BtnStop(navController: NavHostController, timerCounter: ITimerCounter)
{
    // 計測ストップ状態時のボタン配置
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  記録一覧画面へ遷移  ////////////////////
        Button(
            id = R.drawable.baseline_list_24,
            contentDescription = "List",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { navController.navigate("RecordListScreen") },
            enabled = true,
        )

        ////////////////////  カウンター表示の切り替え  ////////////////////
        Button(
            id = R.drawable.baseline_swap_vert_24,
            contentDescription = "ChangeScreen",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { timerCounter.toggleCounterMode() },
            enabled = true
        )

        ////////////////////  カウンターのスタート  ////////////////////
        Button(
            id = R.drawable.baseline_play_arrow_24,
            contentDescription = "Start",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { timerCounter.start() },
            enabled = true
        )
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun BtnStart(timerCounter: ITimerCounter, context: Context, isEnable: Boolean)
{
    // タイム計測実行中時のボタン配置
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  カウンターのストップ (長押し)  ////////////////////
        Button(
            id = R.drawable.baseline_stop_24,
            contentDescription = "Stop",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                Toast.makeText(context, context.getString(R.string.long_press_to_stop), Toast.LENGTH_SHORT).show()
            },
            onLongClick = {
                //Log.v("STOP", "STOP: onLongClick (2)")
                timerCounter.stop()
            },
            enabled = true
        )

        ////////////////////  カウンター表示の切り替え  ////////////////////
        Button(
            id = R.drawable.baseline_swap_vert_24,
            contentDescription = "ChangeScreen",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { timerCounter.toggleCounterMode() },
            enabled = true
        )

        ////////////////////  タイムスタンプ記録 (チャタリング防止機能あり)  ////////////////////
        Button(
            id = R.drawable.baseline_flag_24,
            contentDescription = "Lap",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = if (isEnable) { Color.White } else { Color.DarkGray }
            ),
            onClick =
            {
                if (isEnable)
                {
                    // ボタンは常時有効にして、ラップが押せる時間が経過したらボタンの処理を実行する
                    // （horologist-compose のボタン仕様に合わせて変更...）
                    timerCounter.timeStamp(false)
                }
            },
            enabled = true
        )
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun BtnFinished(navController: NavHostController, timerCounter: ITimerCounter)
{
    // 計測終了時のボタン
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  記録一覧画面へ遷移  ////////////////////
        Button(
            id = R.drawable.baseline_list_24,
            contentDescription = "List",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { navController.navigate("RecordListScreen") },
            enabled = true,
        )

        ////////////////////  カウンターのリセット  ////////////////////
        Button(
            id = R.drawable.baseline_refresh_24,
            contentDescription = "Reset",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { timerCounter.reset() },
            enabled = true
        )

        ////////////////////  カウンターのスタート  ////////////////////
        Button(
            id = R.drawable.baseline_play_arrow_24,
            contentDescription = "Start",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { timerCounter.start() },
            enabled = true
        )
    }
}
