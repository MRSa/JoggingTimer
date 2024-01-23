package net.osdn.gokigen.joggingtimer.presentation.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import net.osdn.gokigen.joggingtimer.ResultListData

@Composable
fun DetailControlButtons(context: Context, navController: NavHostController, indexId: Int, dataItem: ResultListData, lapData: ArrayList<LapTimeDataItem>)
{
    // 詳細画面の操作ボタン
    Row(modifier = Modifier
        .padding(horizontal = 5.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  データの編集  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {  },
            enabled = true,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_black_24dp),
                contentDescription = "Edit",
                tint = Color.LightGray
            )
        }

        ////////////////////  データの共有  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {
                ShareContent.shareLapTimeData(context, dataItem, lapData)
                Toast.makeText(context, context.getString(R.string.intent_issued), Toast.LENGTH_SHORT).show()  // UIスレッドで実行が必要
            },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share_black_24dp),
                contentDescription = "Share",
                tint = Color.LightGray
            )
        }

        ////////////////////  データの削除  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {  },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.LightGray
            )
        }
    }
}
