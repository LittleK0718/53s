package com.example.a53session1

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview
@Composable
fun ckeck(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(12.dp))
    )
{

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