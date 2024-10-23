package com.example.a53session1

import android.annotation.SuppressLint
import android.content.Context
import android.media.effect.Effect
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Button
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.material.Scaffold as Scaffold
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Square
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.Text as Text

class MainActivity : ComponentActivity() {
    private lateinit var paymentMethods: List<String>
    private lateinit var mediaCenter: List<MediaCenter>
    private lateinit var tickets: List<Ticket>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentMethods =
            readJsonFromFile(this, R.raw.paymentmethod, object : TypeToken<List<String>>() {})
        mediaCenter =
            readJsonFromFile(this, R.raw.mediacenter, object : TypeToken<List<MediaCenter>>() {})
        tickets = readJsonFromFile(this, R.raw.tickets, object : TypeToken<List<Ticket>>() {})

        val db = AccountDatabase.getDatabase(this)
        val accountDao = db.accountDao()
        setContent {
            AccountInputScreen(accountDao)
            nav(mediaCenter)
        }
    }
}


data class FirstArt(
    val title: String,
    val content: String,
    val image: String
)

fun readJsonFromAssets(context: Context): FirstArt?{
    // 使用 assets.open() 讀取檔案
    val inputStream = context.assets.open("firstArt.json")

    // 使用 InputStreamReader 來讀取 input stream 並轉換成字符串
    val reader = InputStreamReader(inputStream)

    // 使用 Gson 將 JSON 轉換為 Person 物件
    val gson = Gson()
    return gson.fromJson(reader, FirstArt::class.java)
}


data class SecondArt(
    val title: String,
    val content: String,
    val image: String
)

fun readJsonFromAssets2(context: Context): SecondArt?{
    // 使用 assets.open() 讀取檔案
    val inputStream = context.assets.open("secondArt.json")

    // 使用 InputStreamReader 來讀取 input stream 並轉換成字符串
    val reader = InputStreamReader(inputStream)

    // 使用 Gson 將 JSON 轉換為 Person 物件
    val gson = Gson()
    return gson.fromJson(reader, SecondArt::class.java)
}

@Composable
fun nav(announcements: List<MediaCenter>) {
    val scaffoldState = rememberScaffoldState() // Scaffold 包含 drawerState
    val scope = rememberCoroutineScope()
    val navController = rememberNavController() // 建立 navController
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                {

                    Image(
                        painter = painterResource(R.drawable.a4),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(x = -30.dp)

                    )

                }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                    )
                    {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            tint = Color(0xFFf11617F),
                        )
                    }

                }, backgroundColor = Color.White, elevation = 0.dp
            )

        }, drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
                    .offset(y = 10.dp, x = 20.dp)
            ) {
                TextButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    navController.navigate("home")
                }
                )
                {
                    Icon(
                        Icons.Default.Square,
                        contentDescription = null,
                        tint = Color(0xFFf11617F)
                    )
                    Text(
                        "關於展館",
                        modifier = Modifier,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }

                TextButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    navController.navigate("3D")
                }
                )
                {
                    Icon(
                        Icons.Default.Square,
                        contentDescription = null,
                        tint = Color(0xFFf11617F),
                    )
                    Text(
                        "樓層立體圖",
                        modifier = Modifier,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                TextButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    navController.navigate("Artscreen/{title}/{content}/{image}")
                }
                )
                {
                    Icon(
                        Icons.Default.Square,
                        contentDescription = null,
                        tint = Color(0xFFf11617F),
                    )
                    Text(
                        "公共藝術",
                        modifier = Modifier,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
                TextButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    navController.navigate("call")
                }
                )
                {
                    Icon(
                        Icons.Default.Square,
                        contentDescription = null,
                        tint = Color(0xFFf11617F),
                    )
                    Text(
                        "聯絡我們",
                        modifier = Modifier,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
        }, content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        homeScreen(navController, announcements)
                    }
                    composable("3D") {
                        Float3D()
                    }
                    composable("call"){
                        callme()
                    }
                    composable("detail/{title}/{dateTime}/{hall}/{content}") { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title")
                        val dateTime = backStackEntry.arguments?.getString("dateTime")
                        val hall = backStackEntry.arguments?.getString("hall")
                        val content = backStackEntry.arguments?.getString("content")
                        // 顯示詳細頁面
                        card(navController, title, dateTime, hall, content)
                    }

                    composable("Artscreen/{title}/{content}/{image}"){ backStackEntry ->

                        val title = backStackEntry.arguments?.getString("title")
                        val content = backStackEntry.arguments?.getString("content")
                        val image = backStackEntry.arguments?.getString("image")


                        val first = FirstArt(title ?: "", content ?: "", image ?: "")
                        Artscreen(first,)
                    }
                }
            }
        }
    )
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun homeScreen(navController: NavController, announcements: List<MediaCenter>) {
    var num1 by remember { mutableStateOf(false) }

    val list = listOf(
        R.drawable.view1,
        R.drawable.view2
    )

    val pagerState = rememberPagerState(pageCount = {
        list.size
    })

        HorizontalPager(state = pagerState) { page ->
            Image(
                painter = painterResource(id = list[page]),
                contentDescription = "Page $page",
                modifier = Modifier
                    .size(400.dp)
                    .offset(y = -80.dp)
            )
        }
        Row{
        Text(
            "媒體中心",
            Modifier
                .offset(x = 30.dp, y = 260.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
            Text(
                text = "滑到第 ${pagerState.currentPage + 1} 頁",
                modifier = Modifier
                    .padding(start = 200.dp, top = 250.dp)
                    .size(50.dp)
            )
        }
        Box{
        LazyColumn(
            Modifier
                .fillMaxSize()
                .offset(y = 300.dp, x = 10.dp)
        ) {
            items(announcements) { announcement ->
                Row {
                    Column {
                        Text(
                         text = announcement.dateTime
                        //                            ,Modifier.clickable() {
//                                navController.navigate(
//                                    "detail/${announcement.title}/${announcement.dateTime}/${
//                                        announcement.hall.joinToString(
//                                            ", "
//                                        )
//                                    }/${announcement.content}"
//                                )
//                            }
                        )
                        Text(
                            text = announcement.hall.joinToString(", ")
                        )

                    }

                    ClickableText(
                        text = AnnotatedString(announcement.title),
                        onClick = {
                            navController.navigate(
                                "detail/${announcement.title}/${announcement.dateTime}/${
                                    announcement.hall.joinToString(
                                        ", "
                                    )
                                }/${announcement.content}"
                            )
                        },
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }
    }



}


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

data class MediaCenter(
    val title: String,
    val dateTime: String,
    val hall: List<String>,
    val content: String
)

data class Ticket(
    val ticketType: String,
    val price: Int
)

private fun <T> readJsonFromFile(context: Context, resourceId: Int, typeToken: TypeToken<T>): T {
    val inputStream = context.resources.openRawResource(resourceId)
    val json = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    return gson.fromJson(json, typeToken.type)
}


@Composable
fun card(navController: NavController, title: String?, dateTime: String?, hall: String?, content: String?)
{
    Column {
        IconButton(onClick = {
            navController.navigate("home")
        }) {
            Icon(
                Icons.Default.ArrowBackIos,
                contentDescription = null,
                tint = Color(0xFFf11617F)
            )
        }
        Text(
            text = "$title",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp, start = 20.dp, end = 20.dp)
        )
        Row {
            Text(text = "$hall", fontSize = 18.sp, modifier = Modifier.padding(start = 17.dp))
            Text(
                text = "發文日期: $dateTime",
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 110.dp)
            )
        }
        Text(
            text = "$content",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 25.dp, end = 25.dp)
                .offset(y = 15.dp)
        )
    }
}

@Composable
fun Artscreen(firstArt: FirstArt){
    val button1Pressed = remember { mutableStateOf(true) }
    val button2Pressed = remember { mutableStateOf(false) }
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

    Box(
        Modifier
            .fillMaxSize()
    ){
    if(button1Pressed.value) {
        Column {
            Text(text = "Title: ${firstArt.title}")
            Text(text = "Content: ${firstArt.content}")
            Text(text = "Image: ${firstArt.image}")
        }
    }
    }
}

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
Column {
        Box (modifier = Modifier
            .padding(4.dp)
            .padding(start = 30.dp)
            .clip(RoundedCornerShape(12.dp))  // 設置圓角
            .border(
                BorderStroke(2.dp, Color(0xFFf11617F)), RoundedCornerShape(12.dp)
            )
        )
        {
            TextField(
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
        TextField(
            placeholder = { Text("姓名") },
            value = name,
            onValueChange = { newname ->
                if(newname.length<=maxname){
                    name = newname
                }
            },
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
        TextField(
            value = phonenum,
            placeholder = { Text("電話") },
            onValueChange = { phonenum = it },

                    modifier = Modifier
                .background(color = Color.Transparent)
        )
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
        TextField(
            placeholder = { Text("E-mail") },
            value = email,
            onValueChange = { new ->
                if(new.length<=maxLength){
                    email = new
                }
            },
            isError = name.length>maxname,
            modifier = Modifier
                .background(color = Color.Transparent),
        )
        if (email.length >= maxLength) {
            Text(
                text = "email不能超過30個字！",
                color = Color.Red,  // 設置錯誤訊息顯示為紅色
                modifier = Modifier.padding(top = 4.dp, start = 1.dp)
            )
        }
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
    )
    {
        TextField(
            placeholder = { Text("內容") },
            value = what,
            onValueChange = { what=it
            },
            isError = name.length>maxname,
            modifier = Modifier
                .background(color = Color.Transparent),
        )
}
}
}

@Composable
fun AccountInputScreen(accountDao: AccountDao) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation() // 隱藏密碼輸入
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // 當按下按鈕時，將帳號和密碼存入資料庫
                val newAccount = Account(username = username, password = password)
                GlobalScope.launch {
                    accountDao.insertAccount(newAccount)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}


