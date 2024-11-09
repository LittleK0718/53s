package com.example.a53session1

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun nav2(announcements: List<MediaCenter>,accountDao:AccountDao,tic:List<Ticket>,
         viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(accountDao))
){
    val navController2 = rememberNavController() // 建立 navController

    NavHost(navController = navController2,startDestination = "acclogin")
    {
        composable("navhost2"){
            nav(announcements,tic)
        }
        composable("accsgin"){
            AccountInputScreen(accountDao,navController2)
        }
        composable("acclogin"){
            Accountlogin(navController2,accountDao)
        }
        composable("wait"){
            delay(navController2)
        }
    }
}



//註冊畫面

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun AccountInputScreen(accountDao: AccountDao,navController: NavController) {
    var usermail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordTure by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.a4)
            , contentDescription = null
            , modifier = Modifier
        )

        Spacer(Modifier.height(30.dp))
        Text(
            text = "註冊",
            Modifier.offset(x=130.dp),
            color = Color(0xFFf1AAB9F)
            , fontSize = 40.sp
            , fontWeight = FontWeight.Bold
            ,letterSpacing = 10.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
        //Mail
        OutlinedTextField(
            value = usermail,
            onValueChange = { usermail = it },
            label = {
                Text(
                    text = "輸入E-mail",
                    color = Color(0xFFf1AAB9F)
                )
            },
            leadingIcon={
                Image(
                    painter = painterResource(R.drawable.signmail),
                    contentDescription = null
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
            , colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFf1AAB9F)
                ,focusedBorderColor = Color (0xFFf1AAB9F)
                ,textColor = Color.Black, // 輸入文字顏色
            )
        )


        Spacer(modifier = Modifier.height(12.dp))

        //password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密碼", color = Color(0xFFf1AAB9F)) },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.sign)
                    , contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
            ,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFf1AAB9F)
                ,focusedBorderColor = Color (0xFFf1AAB9F)
                ,textColor = Color.Black, // 輸入文字顏色
            )
            ,
            visualTransformation = PasswordVisualTransformation() // 隱藏密碼輸入
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = passwordTure,
            onValueChange = { passwordTure = it },
            label = { Text("確認密碼", color = Color(0xFFf1AAB9F)) },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.sign)
                    , contentDescription =null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
            ,visualTransformation = PasswordVisualTransformation() // 隱藏密碼輸入
            , colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFf1AAB9F)
                ,focusedBorderColor = Color (0xFFf1AAB9F)
                ,textColor = Color.Black, // 輸入文字顏色

            )

        )
        Spacer(modifier = Modifier.height(100.dp))


        Button(
            onClick = {
                GlobalScope.launch {
                    when {
                        usermail.isBlank() || password.isBlank() -> {
                            errorMessage = "E-mail 和密碼不得為空白！"
                        }

                        usermail.length > 30 -> {
                            errorMessage = "E-mail 不能超過 30 字元！"
                        }

                        !isValidEmail(usermail) -> {
                            errorMessage = "E-mail 格式不正確！"
                        }

                        !isValidPassword(password) -> {
                            errorMessage = "密碼僅允許英文與數字，且至少 6 碼！"
                        }

                        password != passwordTure -> {
                            errorMessage = "密碼不符！"
                        }

                        accountDao.isAccExists(usermail, password) -> {
                            errorMessage = "此 E-mail 已註冊過！"
                        }

                        else -> {

                            // 所有檢查通過，插入帳號到資料庫
                            val newAccount = Account(usermail = usermail, password = password)
                            GlobalScope.launch {
                                accountDao.insertAccount(newAccount)
                            }
                            navController.navigate("acclogin")
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(12.dp))
                .height(40.dp)
            , colors = ButtonDefaults.buttonColors(Color(0xFFf1AAB9F))

        ) {
            Text("註冊", color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp, start = 1.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
            Text(text = "已有帳號了! ")

            Text(
                text = "登入"
                , modifier = Modifier.clickable(onClick = {
                    navController.navigate("acclogin")
                }), Color(0xFFf1AAB9F)
            )
        }
    }
}

//登入畫面

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Accountlogin(navController: NavController,accountDao: AccountDao){
    var usermail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.a4), contentDescription = null, modifier = Modifier
        )

        Spacer(Modifier.height(30.dp))
        Text(
            text = "登入",
            Modifier.offset(x = 130.dp),
            color = Color(0xFFf11617f),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 10.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
        //Mail
        OutlinedTextField(
            value = usermail,
            onValueChange = { usermail = it },
            label = {
                Text(
                    text = "輸入E-mail",
                    color = Color(0xFFf11617f)
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.loginmail),
                    contentDescription = null
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFf11617f),
                focusedBorderColor = Color(0xFFf11617f),
                textColor = Color.Black, // 輸入文字顏色
            )
        )


        Spacer(modifier = Modifier.height(12.dp))

        //password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密碼", color = Color(0xFFf11617f)) },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.login), contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFf11617f),
                focusedBorderColor = Color(0xFFf11617f),
                textColor = Color.Black, // 輸入文字顏色
            ),
            visualTransformation = PasswordVisualTransformation() // 隱藏密碼輸入
        )


        Spacer(modifier = Modifier.height(100.dp))

        OutlinedButton(onClick = {
            GlobalScope.launch {
                val exists = accountDao.isAccExists(usermail, password)
                withContext(Dispatchers.Main) { // 切換到主線程
                    if (exists) {
                        navController.navigate("wait")
                    } else {
                        errorMessage = "帳號不存在"
                    }
                }

            }
        }
            , modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp), colors = ButtonDefaults.buttonColors(Color(0xFFf11617f))
        )

        { Text("登入", color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp, start = 1.dp)
            )
        }


        Spacer(modifier = Modifier.height(80.dp))
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
            Text(text = "沒有任何帳號嗎? ")

            Text(
                text = "註冊"
                , modifier = Modifier.clickable(onClick = {
                    navController.navigate("accsgin")
                }),Color(0xFFf1AAB9F)
            )
        }

    }
}

//延遲畫面

@Composable
fun delay(navController: NavController){
    LaunchedEffect(Unit) {
        // 延遲 3 秒
        kotlinx.coroutines.delay(3000L)
        // 3 秒後導航回上一個頁面
        navController.navigate("navhost2")
    }
    Box{
        Image(
            painter = painterResource(R.drawable.wait5)
            , contentDescription = null
            , modifier = Modifier
                .size(500.dp)
                .align(Alignment.Center)
        )
    }
}

//正規表達(看不懂)

// 驗證 E-mail 格式的函數
fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
    return emailRegex.matches(email)
}

// 驗證密碼格式的函數
fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex("^[A-Za-z0-9]{6,}$")
    return passwordRegex.matches(password)
}

fun isValidphone(phone:String):Boolean{
    val num=Regex("^09[0-9]{8}")
    return num.matches(phone)
}
