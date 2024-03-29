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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.ResultListData
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider

@Composable
fun DetailControlButtons(context: Context, navController: NavHostController, indexId: Int, dataItem: ResultListData, lapData: ArrayList<LapTimeDataItem>)
{
    val deleteDialog = remember { mutableStateOf(false) }
    val iconId = remember { dataItem.iconId }

    // 詳細画面の操作ボタン
    Row(modifier = Modifier
        .padding(vertical = 0.dp, horizontal = 0.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  データのアイコン  ////////////////////
        val drawableIconId = IconIdProvider.getIconResourceId(iconId)
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(30.dp)
                .padding(0.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {
                navController.navigate("SetReferenceScreen/${dataItem.indexId}")
            },
            shape = RoundedCornerShape(0.dp),
            enabled = true,
        ) {
            Icon(
                painter = painterResource(id = drawableIconId),
                contentDescription = "Icon",
                tint = Color(0xffaaaaaa)
            )
        }

        ////////////////////  データの編集  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(0.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {
                navController.navigate("RecordEditScreen/${dataItem.indexId}")
            },
            shape = RoundedCornerShape(0.dp),
            enabled = true,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_black_24dp),
                contentDescription = "Edit",
                tint = Color.White
            )
        }

        ////////////////////  データの共有  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(0.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {
                ShareContent.shareLapTimeData(context, dataItem, lapData)
                Toast.makeText(context, context.getString(R.string.intent_issued), Toast.LENGTH_SHORT).show()  // UIスレッドで実行が必要
            },
            shape = RoundedCornerShape(0.dp),
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share_black_24dp),
                contentDescription = "Share",
                tint = Color.White
            )
        }

        ////////////////////  データの削除  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(0.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { deleteDialog.value = true }, // Delete Confirmation
            shape = RoundedCornerShape(0.dp),
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }

    // データ削除の確認
    Dialog(showDialog = deleteDialog.value, onDismissRequest = { deleteDialog.value = false })
    {
        Alert(
            //icon = {
            //    Icon(
            //        painter = painterResource(id = R.drawable.ic_warning_black_24dp),
            //        contentDescription = "Confirmation",
            //        tint = MaterialTheme.colors.secondary
            //    )
            //},
            title = {
                Text(
                    text = stringResource(id = R.string.dialog_message_delete),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            message = {
                Text(
                    text =  " " + dataItem.title,
                    color = MaterialTheme.colors.secondary,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
        ) {
            item {
                Chip(
                    modifier = Modifier
                        //.fillMaxSize()
                        .height(48.dp)
                        .width(96.dp)
                        .background(color = Color.Black),
                    label = {
                        Text(
                            text = stringResource(id = R.string.dialog_positive_execute),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                            ) },
                    onClick = {
                        deleteDialog.value = false
                        AppSingleton.controller.deleteRecord(indexId)
                        navController.popBackStack()  // 前の画面に戻る
                        },
                    colors = ChipDefaults.primaryChipColors(),
                )
            }
            item {
                Chip(
                    modifier = Modifier
                        //.fillMaxSize()
                        .height(48.dp)
                        .width(96.dp)
                        .background(color = Color.Black),
                    label = {
                        Text(
                            stringResource(id = R.string.dialog_negative_cancel),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                            ) },
                    onClick = { deleteDialog.value = false },
                    colors = ChipDefaults.secondaryChipColors(
                        backgroundColor = Color.LightGray
                    ),
                )
            }
        }
    }
}
