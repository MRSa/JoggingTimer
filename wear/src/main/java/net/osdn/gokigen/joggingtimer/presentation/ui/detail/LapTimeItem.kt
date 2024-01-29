package net.osdn.gokigen.joggingtimer.presentation.ui.detail

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase.DEFAULT_RECORD_TYPE
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase.PASSAGE_RECORD_TYPE
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LapTimeItem(context: Context, navController: NavHostController, lapCount: Int, data: LapTimeDataItem)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            //text = "[$lapCount] ${TimeStringConvert.getTimeString(data.lapTime)} (${TimeStringConvert.getDiffTimeString(data.diffTime)})",
            text = "[%02d] %s (%s)".format(lapCount, TimeStringConvert.getTimeString(data.lapTime), TimeStringConvert.getDiffTimeString(data.diffTime)),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .combinedClickable(
                    enabled = true,
                    onClick = {
                        if (data.recordType.toLong() == DEFAULT_RECORD_TYPE)
                        {
                            // 記録： 編集できません
                            //Toast.makeText(context, context.getString(R.string.cannot_edit_record), Toast.LENGTH_SHORT).show()
                        }
                        else if (data.recordType.toLong() == PASSAGE_RECORD_TYPE)
                        {
                            // 通過： 編集できません
                            //Toast.makeText(context, context.getString(R.string.cannot_edit_record), Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            // 長押しで編集
                            Toast.makeText(context, context.getString(R.string.long_press_to_edit), Toast.LENGTH_SHORT).show()
                        }
                    },
                    onLongClick = {
                        if (data.recordType.toLong() == DEFAULT_RECORD_TYPE)
                        {
                            // 記録： 編集できません
                            //Toast.makeText(context, context.getString(R.string.cannot_edit_record), Toast.LENGTH_SHORT).show()
                        }
                        else if (data.recordType.toLong() == PASSAGE_RECORD_TYPE)
                        {
                            // 通過： 編集できません
                            //Toast.makeText(context, context.getString(R.string.cannot_edit_record), Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            // 長押しで編集
                            // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                            //Toast.makeText(context, context.getString(toastTextId), Toast.LENGTH_SHORT).show()
                            Log.v("EDIT", "EDIT: onLongClick")
                        }
                    }
                ),
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
            color = if (data.recordType.toLong() == DEFAULT_RECORD_TYPE) { Color.White } else { Color.LightGray }
        )
        //Divider(color = Color.DarkGray, thickness = 1.dp)
    }
}
