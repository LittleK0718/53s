package com.example.a53session1

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Float3D() {
    val button1Pressed = remember { mutableStateOf(true) }
    val button2Pressed = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                button1Pressed.value = true
                button2Pressed.value = false
            }, modifier = Modifier
                .offset(x = 25.dp, y = 10.dp)
                .border(BorderStroke(2.dp, Color.Black))
                .width(70.dp)
                .height(40.dp), colors = ButtonDefaults.buttonColors(
                backgroundColor = if (button1Pressed.value) Color(0xFFf11617F) else Color.Transparent // 根據按鈕狀態改變背景顏色
            )
        ) {
            Text(
                "一館",
                color = if (button1Pressed.value) Color.White else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }
        Button(
            onClick = {
                button2Pressed.value = true
                button1Pressed.value = false
            }, modifier = Modifier
                .offset(x = 125.dp, y = 10.dp)
                .border(BorderStroke(2.dp, Color.Black))
                .width(70.dp)
                .height(40.dp), colors = ButtonDefaults.buttonColors(
                backgroundColor = if (button2Pressed.value) {
                    Color(0xFFf11617F)
                } else Color.Transparent
            )
        ) {
            Text(
                "二館",
                color = if (button2Pressed.value) Color.White else Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }
        if (button1Pressed.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.house1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .offset(y = 40.dp), contentScale = ContentScale.Crop

                )
            }
        }
        if (button2Pressed.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.house2),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .offset(y = 40.dp), contentScale = ContentScale.Crop

                )
            }
        }
    }
}