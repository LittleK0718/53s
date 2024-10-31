package com.example.a53session1

import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import java.io.InputStreamReader

@Composable
fun callme() {
    var title by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var phonenum by remember {
        mutableStateOf("")
    }
    val maxLength=30
    val maxname=15
    Column (Modifier.padding(top = 30.dp)){
        Box (modifier = Modifier
            .padding(4.dp)
            .padding(start = 30.dp)
            .clip(RoundedCornerShape(12.dp))  // 設置圓角
            .border(
                BorderStroke(2.dp, Color(0xFFf11617F)), RoundedCornerShape(12.dp)
            )
        )
        {
            OutlinedTextField(
                value = title,
                placeholder = { Text("標題") },
                onValueChange = { newTitle ->
                    if (newTitle.length <= maxLength) { // 檢查是否超過最大字數
                        title = newTitle }},
                isError = title.length > maxLength,
                modifier = Modifier
                    .background(color = Color.Transparent)

            )
            if (title.length >= maxLength) {
                Text(
                    text = "標題不能超過 $maxLength 個字！",
                    color = Color.Red,  // 設置錯誤訊息顯示為紅色
                    modifier = Modifier.padding(top = 4.dp, start = 2.dp)
                )

            }
        }


        Box (modifier = Modifier
            .padding(4.dp)
            .padding(start = 30.dp)
            .clip(RoundedCornerShape(12.dp))  // 設置圓角
            .border(
                BorderStroke(2.dp, Color(0xFFf11617F)), RoundedCornerShape(12.dp)
            )
        )
        {
            OutlinedTextField(
                placeholder = { Text("姓名") },
                value = name,
                onValueChange = { name = it },
                isError = name.length>maxname,
                modifier = Modifier
                    .background(color = Color.Transparent),
            )
            if (name.length >= maxname) {
                Text(
                    text = "姓名不能超過 $maxname 個字！",
                    color = Color.Red,  // 設置錯誤訊息顯示為紅色
                    modifier = Modifier.padding(top = 4.dp, start = 1.dp)
                )
            }
        }


        Box (modifier = Modifier
            .padding(4.dp)
            .padding(start = 30.dp)
            .clip(RoundedCornerShape(12.dp))  // 設置圓角
            .border(
                BorderStroke(2.dp, Color(0xFFf11617F)), RoundedCornerShape(12.dp)
            )
        )
        {
            OutlinedTextField(
                value = phonenum,
                placeholder = { Text("電話") },
                onValueChange = { phonenum=it
                },modifier = Modifier.background(color = Color.Transparent)
                , isError = phonenum.length> 10
            )
            if(phonenum.isNotEmpty()){
                when{
                    phonenum.length >10->{
                        Text(
                            text = "號碼不能超過10個字！",
                            color = Color.Red,  // 設置錯誤訊息顯示為紅色
                            modifier = Modifier.padding(top = 4.dp, start = 2.dp)
                        )}
                    !isValidphone(phonenum) ->{
                        Text(
                            text = "號碼格式不對！",
                            color = Color.Red,  // 設置錯誤訊息顯示為紅色
                            modifier = Modifier.padding(top = 4.dp, start = 2.dp)
                        )
                    }
                }}
        }

        Box (modifier = Modifier
            .padding(4.dp)
            .padding(start = 30.dp)
            .clip(RoundedCornerShape(12.dp))  // 設置圓角
            .border(
                BorderStroke(2.dp, Color(0xFFf11617F)), RoundedCornerShape(12.dp)
            )
        )
        {
            OutlinedTextField(
                placeholder = { Text("E-mail") },
                value = email,
                onValueChange = { email=it },
                isError = email.length>maxLength,
                modifier = Modifier
                    .background(color = Color.Transparent),
            )
            if(email.isNotEmpty()){
                when{
                    email.length > maxLength->{
                        Text(
                            text = "email不能超過30個字！",
                            color = Color.Red,  // 設置錯誤訊息顯示為紅色
                            modifier = Modifier.padding(top = 4.dp, start = 1.dp)
                        )}
                    !isValidEmail(email)->{
                        Text(
                            text = "email格式不對！",
                            color = Color.Red,  // 設置錯誤訊息顯示為紅色
                            modifier = Modifier.padding(top = 4.dp, start = 1.dp)
                        )
                    }
                }}
        }

        var what by remember {
            mutableStateOf("")
        }
        Box (modifier = Modifier
            .padding(4.dp)
            .padding(start = 30.dp, end = 30.dp)
            .clip(RoundedCornerShape(12.dp))  // 設置圓角
            .border(
                BorderStroke(2.dp, Color(0xFFf11617F)), RoundedCornerShape(12.dp)
            )
        ){
            OutlinedTextField(
                placeholder = { Text("內容") },
                value = what,
                onValueChange = { what=it
                },
                isError = name.length>maxname,
                modifier = Modifier
                    .background(color = Color.Transparent),
            )
        }
        Row(Modifier.padding(start = 70.dp, top = 30.dp)) {

            Button(onClick = {
                title=""
                phonenum=""
                name=""
                email=""
                what=""
            }, modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(Color(0xFFf11617F))
            )
            { Text(
                "重新填寫", color = Color.White
            )
            }

            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = {

            }, modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(Color(0xFFf11617F))
            )
            { Text(
                "送出", color = Color.White
            )
            }}
    }
}