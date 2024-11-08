package com.example.a53session1

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Button
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.material.Scaffold as Scaffold
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Square
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material.Text as Text
import androidx.compose.ui.res.painterResource as painterResource

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
        tickets =
            readJsonFromFile(this, R.raw.tickets, object : TypeToken<List<Ticket>>() {})

        val db = AccountDatabase.getDatabase(this)
        val accountDao = db.accountDao()
        setContent {

            nav2(mediaCenter, accountDao, tickets,)
        }
    }
}

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
class AccountViewModel(private val accountDao: AccountDao) : ViewModel() {
    // 使用 MutableState<Boolean> 來正確地保持 UI 狀態
    private val _accountExists = mutableStateOf(false)
    val accountExists: MutableState<Boolean> = _accountExists

    // 檢查帳號是否存在
    fun checkAccountExists(usermail: String,password: String) {
        viewModelScope.launch {
            _accountExists.value = accountDao.isAccExists(usermail,password)
        }
    }
}


@Composable
fun nav(announcements: List<MediaCenter>,tic:List<Ticket>) {
    val scaffoldState = rememberScaffoldState() // Scaffold 包含 drawerState
    val scope = rememberCoroutineScope()
    val navController = rememberNavController() // 建立 navController
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if(currentRoute != "detail/{title}/{dateTime}/{hall}/{content}"&&currentRoute!="wait"&&currentRoute!="ticket") {
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
            }
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

                Column(Modifier.offset(x=20.dp)) {
                    TextButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.navigate("webview/展館介紹.html")
                    }
                    )
                    {
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color(0xFFf11617F),
                            modifier = Modifier.size(20.dp).border(
                                BorderStroke(2.dp, Color(0xFFf11617F)),
                                RoundedCornerShape(12.dp)
                            )
                        )
                        Text(
                            "展館介紹",
                            modifier = Modifier,
                            fontSize = 18.sp,
                            color = Color.Black,

                        )
                    }

                    TextButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.navigate("webview/經營者.html")
                    }
                    )
                    {
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color(0xFFf11617F),
                            modifier = Modifier.border(
                                BorderStroke(2.dp, Color(0xFFf11617F)),
                                RoundedCornerShape(12.dp)
                            ).size(20.dp)
                        )
                        Text(
                            "經營者",
                            modifier = Modifier,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }

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
                    navController.navigate("Artscreen")
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
                        "主畫面",
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

                    composable("Artscreen"){
//                        backStackEntry ->
//                        val title = backStackEntry.arguments?.getString("title")
//                        val content = backStackEntry.arguments?.getString("content")
//                        val image = backStackEntry.arguments?.getString("image")
                        val context = LocalContext.current
//                        val first = FirstArt(title ?: "", content ?: "", image ?: "")
                        Artscreen(context)
                    }
                    composable("ticket"){
                        ticket(navController,tic)
                    }
                  composable("check"){
                      check()
                  }
                    composable("webview/{htmlFile}") { backStackEntry ->
                        val htmlFile = backStackEntry.arguments?.getString("htmlFile")
                        WebViewScreen(htmlFile)
                    }

                }
            }
        }
    )
}

class AccountViewModelFactory(private val accountDao: AccountDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(accountDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



@Composable
fun nav2(announcements: List<MediaCenter>,accountDao:AccountDao,tic:List<Ticket>,
         viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(accountDao))){
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






@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun homeScreen(navController: NavController, jsonlist: List<MediaCenter>) {
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
                .size(350.dp)
                .offset(y = -80.dp, x = 20.dp)
        )
    }
    Row {
        Text(
            "媒體中心",
            Modifier
                .offset(x = 10.dp, y = 200.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "滑到第 ${pagerState.currentPage + 1} 頁",
            modifier = Modifier
                .padding(start = 250.dp, top = 200.dp)
                .size(50.dp)
        )
    }
    Box {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .offset(y = 220.dp, x = 10.dp)
        ) {
            items(jsonlist) { abc ->
                Row {
                    Column(Modifier.offset(y = 12.dp)) {
                        Text(
                            text = abc.dateTime
                        )
                        Text(
                            text = abc.hall.joinToString(", "),
                            textAlign = TextAlign.Center,
                            color = Color(0xFFf1AAB9F)
                        )

                    }

                    Text(
                        text = AnnotatedString(abc.title),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable(onClick = {
                                navController.navigate(
                                    "detail/${abc.title}/${abc.dateTime}/${
                                        abc.hall.joinToString(
                                            ", "
                                        )
                                    }/${abc.content}"
                                )
                            }),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }


        Column(
            modifier = Modifier
                .padding(16.dp)
                .offset(y = 550.dp)
        ) {
            Text(
                text = "購票資訊",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(90.dp)
                    .border(BorderStroke(2.dp, Color.Black), (RoundedCornerShape(10.dp)))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "2023第41屆新一代設計展",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "YODEX",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier
                    )


                }
                Row(
                    modifier = Modifier
                        .offset(y = -20.dp)
                        .size(100.dp)
                        .offset(y = 50.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { navController.navigate("ticket") },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))  // 設置圓角
                            .border(
                                BorderStroke(2.dp, Color.Black), RoundedCornerShape(12.dp)
                            )

                    ) {
                        Text(text = "購票", color = Color.Black, modifier = Modifier)
                    }
                }
                }
            }
        }
    }


private fun <T> readJsonFromFile(context: Context, resourceId: Int, typeToken: TypeToken<T>): T {
    val inputStream = context.resources.openRawResource(resourceId)
    val json = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    return gson.fromJson(json, typeToken.type)
}


@Composable
fun card(navController: NavController, title: String?, dateTime: String?, hall: String?, content: String?) {
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
                    ,focusedBorderColor =Color (0xFFf1AAB9F)
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
                ,focusedBorderColor =Color (0xFFf1AAB9F)
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
                ,focusedBorderColor =Color (0xFFf1AAB9F)
                ,textColor = Color.Black, // 輸入文字顏色

            )

        )
        Spacer(modifier = Modifier.height(100.dp))


        Button(
            onClick = {
                when {
                    usermail.isBlank() || password.isBlank()  -> {
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
                    accountDao.isAccExists(usermail,password) -> {
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
                }),Color(0xFFf1AAB9F)
            )
        }
    }
}




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

// 檢查 E-mail 是否已註冊的函數
//fun isEmailRegistered(accountDao: AccountDao, email: String): Boolean {
//    // 實作檢查邏輯，這裡需要根據你的資料庫設計來查詢是否有這個 E-mail
//    // 這裡是示範性質，實際上可能需要使用 coroutines 或其他方式來進行資料庫操作
//    return accountDao.getAccountByEmail(email) != null // 假設這個函數返回該 E-mail 是否存在
//}






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

    }}

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

@Composable
fun ticket(navController: NavController, announcements: List<Ticket>){
    var price1 by remember { mutableStateOf(0) }
    var price2 by remember { mutableStateOf(0) }
    var pricekide by remember { mutableStateOf(0) }
    var priceold by remember { mutableStateOf(0) }
    var pricestu by remember { mutableStateOf(0) }
    var error by remember { mutableStateOf("") }

    Column {
        Row{
    IconButton(onClick = {navController.navigate("home")})
    { Icon(Icons.Default.ArrowBackIos, contentDescription = null, tint = Color(0xFFf11617F)) }

        Text("2023第41屆新一代設計展", modifier = Modifier.offset(y = 10.dp), color = Color(0xFFf11617F),
            fontSize = 20.sp
        )
        }
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .size(400.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))

        ) {
        Row {
            Column(Modifier.offset(x = 20.dp)) {
                Text("票種", fontSize = 20.sp, modifier = Modifier.offset(x = 15.dp))

                LazyColumn {
                    items(announcements)
                    { announcements ->

                        Text(
                            "${announcements.ticketType}",
                            color = Color(0xFFf1AAB9F),
                            modifier = Modifier.padding(10.dp),
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Column(
                Modifier
                    .offset(x = 20.dp)
                    .padding(start = 30.dp)) {
                Text("價格", fontSize = 20.sp, modifier = Modifier.offset(x = 10.dp))

                LazyColumn {
                    items(announcements)
                    { announcements ->

                        Text(
                            "${announcements.price}",
                            color = Color(0xFFf1AAB9F),
                            modifier = Modifier.padding(10.dp),
                            fontSize = 20.sp
                        )
                    }
                }
            }

            Column(
                Modifier
                    .offset(x = 20.dp)
                    .padding(start = 30.dp)
            ) {
                Text("數量", fontSize = 20.sp, modifier = Modifier.offset(x = 30.dp))

//1
                Row {
                    IconButton(onClick = { price1 += -1 }) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = -10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                    Text(
                        "${price1}",
                        modifier = Modifier.offset(y = 10.dp),
                        textDecoration = TextDecoration.Underline,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = { price1 += 1 }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = 10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                }
//2
                Row {
                    IconButton(onClick = { price2 += -1 }) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = -10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                    Text(
                        "${price2}",
                        modifier = Modifier.offset(y = 10.dp),
                        textDecoration = TextDecoration.Underline,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = { price2 += 1 }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = 10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                }
//3

                Row {
                    IconButton(onClick = { pricekide += -1 }) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = -10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                    Text(
                        "${pricekide}",
                        modifier = Modifier.offset(y = 10.dp),
                        textDecoration = TextDecoration.Underline,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = { pricekide += 1 }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = 10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                }

//4
                Row {
                    IconButton(onClick = { priceold += -1 }) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = -10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                    Text(
                        "${priceold}",
                        modifier = Modifier.offset(y = 10.dp),
                        textDecoration = TextDecoration.Underline,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = { priceold += 1 }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = 10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                }
//5
                Row {
                    IconButton(onClick = { pricestu += -1 }) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = -10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                    Text(
                        "${pricestu}",
                        modifier = Modifier.offset(y = 10.dp),
                        textDecoration = TextDecoration.Underline,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = { pricestu += 1 }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(x = 10.dp)
                                .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50.dp))
                        )
                    }
                }
            }
        }
    }
    Box(Modifier.fillMaxSize()){
        Button(
            onClick = {
                if(priceold>=1||pricestu>=1||pricekide>=1||price1>=1||price2>=1){
                    navController.navigate("check")
                }else{
                    error="未選擇數量!"
                }

            },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 50.dp, x = 100.dp)
                .clip(RoundedCornerShape(12.dp))
            , colors =ButtonDefaults.buttonColors(Color(0xFFf11617F))
        ) { Text("下一步", color = Color.White) }

        Text(text = "$error",
            modifier = Modifier
                .offset(x = 20.dp)
                .align(Alignment.Center)
                .offset(y = 100.dp, x = 80.dp),
            color = Color.Red,
            fontSize = 20.sp
        )
    }
}
}

