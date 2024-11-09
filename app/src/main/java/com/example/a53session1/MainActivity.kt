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
import kotlinx.coroutines.*
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

private fun <T> readJsonFromFile(context: Context, resourceId: Int, typeToken: TypeToken<T>): T {
    val inputStream = context.resources.openRawResource(resourceId)
    val json = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    return gson.fromJson(json, typeToken.type)
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






