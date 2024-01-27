package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter

///////////////////////////////////////////////////
//  メインのボタンエリアの描画＆制御
//   (JoggingTimer互換のボタン)
///////////////////////////////////////////////////

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
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BtnStart(timerCounter: ITimerCounter, context: Context)
{
    // タイム計測実行中時のボタン配置
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  カウンターのストップ (長押し)  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black)
                .combinedClickable(
                    enabled = true,
                    onClick = {
                        // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                        Toast.makeText(context, context.getString(R.string.long_press_to_stop), Toast.LENGTH_SHORT).show()
                    },
                    onLongClick = {
                        Log.v("STOP", "STOP: onLongClick (2)")
                        timerCounter.stop()
                    }
                ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop",
                tint = Color.LightGray
            )
        }

        ////////////////////  カウンター表示の切り替え  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { timerCounter.timeStamp(false) },
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
    // 計測終了時のボタン
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  記録一覧画面へ遷移  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
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
