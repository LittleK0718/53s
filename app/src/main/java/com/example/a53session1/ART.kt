package com.example.a53session1

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import java.io.InputStreamReader


data class FirstArt(
    val title: String,
    val content: String,
    val image: String
)

fun readJsonFromAssets(context: Context): List<FirstArt>{
    // 使用 assets.open() 讀取檔案
    val inputStream = context.assets.open("first.json")

    // 使用 InputStreamReader 來讀取 input stream 並轉換成字符串
    val reader = InputStreamReader(inputStream)

    // 使用 Gson 將 JSON 轉換為 Person 物件
    val gson = Gson()
    return gson.fromJson(reader,Array<FirstArt>::class.java).toList()
}


data class SecondArt(
    val title: String,
    val content: String,
    val image: String
)

fun readJsonFromAssets2(context: Context): List<SecondArt>{
    // 使用 assets.open() 讀取檔案
    val inputStream = context.assets.open("secondArt.json")

    // 使用 InputStreamReader 來讀取 input stream 並轉換成字符串
    val reader = InputStreamReader(inputStream)

    // 使用 Gson 將 JSON 轉換為 Person 物件
    val gson = Gson()
    return gson.fromJson(reader, Array<SecondArt>::class.java).toList()
}



@Composable
fun Artscreen(context: Context){
    val button1Pressed = remember { mutableStateOf(true) }
    val button2Pressed = remember { mutableStateOf(false) }
    val artinfo = remember { mutableStateOf<List<FirstArt>?>(null) }
    val artinfo2 = remember{ mutableStateOf<List<SecondArt>?>(null) }

    LaunchedEffect(Unit) {
        artinfo.value = readJsonFromAssets(context)
        artinfo2.value = readJsonFromAssets2(context)
    }

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

    when {
        button1Pressed.value -> {
            artinfo.value?.let { artList ->
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 55.dp)) {
                    items(artList) { art ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(text = " ${art.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold )

                            Text(text = art.content, maxLines = 6, modifier = Modifier.padding(end = 200.dp) )

                            val resourceId = context.resources.getIdentifier(art.image.replace(".jpeg", ""), "drawable", context.packageName)

                            // 顯示圖片
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .offset(x = 180.dp, y = -150.dp) // 您可以調整圖片大小
                            )

                        }
                    }
                }
            } ?: Text("Loading...")
        }
        button2Pressed.value -> {
            artinfo2.value?.let { artList ->
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 55.dp)) {
                    items(artList) { art ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(7.dp)
                        ) {
                            Text(text = " ${art.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold )

                            Text(text = art.content, maxLines = 6, modifier = Modifier.padding(end = 200.dp) )

                            val resourceId = context.resources.getIdentifier(art.image.replace(".jpeg", ""), "drawable", context.packageName)

                            // 顯示圖片
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .offset(x = 180.dp, y = -150.dp) // 您可以調整圖片大小
                            )

                        }
                    }
                }
            } ?: Text("Loading...")
        }
    }


}