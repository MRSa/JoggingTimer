package net.osdn.gokigen.joggingtimer.presentation.ui.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.ResultListData
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@Composable
fun ResultListItem(navController: NavHostController, dataItem: ResultListData)
{
    val iconId = IconIdProvider.getIconResourceId(dataItem.iconId)
    val totalTime = TimeStringConvert.getTimeString(dataItem.duration)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Chip(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 2.dp, bottom = 2.dp)
                .background(color = Color.Black),
            onClick = {
                Log.v("Chip", "onClick() id: ${dataItem.indexId}")
                navController.navigate("DetailRecordScreen/${dataItem.indexId}")
            },
            enabled = true,
            icon = {
                Icon(
                    painter = painterResource(id = iconId),
                    tint = Color.DarkGray,
                    contentDescription = "icon",
                    modifier = Modifier.size(ChipDefaults.IconSize)
                        .wrapContentSize(align = Alignment.Center),
                )
            },
            label = {
                Text(
                    text = dataItem.title,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            secondaryLabel = {
                Text(
                    text = totalTime.toString(),
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
        //Divider()
    }
}
