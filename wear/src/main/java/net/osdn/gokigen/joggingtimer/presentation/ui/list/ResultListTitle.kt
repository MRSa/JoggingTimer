package net.osdn.gokigen.joggingtimer.presentation.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.R

@Composable
fun ResultListTitle(navController: NavHostController, message: String)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.result_list),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
        //Divider(color = Color.Gray, thickness = 1.dp)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    start = 5.dp
                )),
            horizontalArrangement = Arrangement.Start,
        ) {
            ////////////////////  基準値設定画面へ遷移  ////////////////////
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    //.padding(5.dp)
                    .background(color = Color.Black),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.primaryButtonColors(backgroundColor = Color.Black),
                onClick = { navController.navigate("CreateReferenceScreen") },
                enabled = true,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_post_add_24),
                    contentDescription = "CreateReference",
                    tint = Color.White
                )
            }
        }
        if (message.isNotEmpty())
        {
            Text(
                text = message,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }
        //Divider(color = Color.Gray, thickness = 1.dp)
    }
}
