package com.kelivo.bridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            KelivoApp()

        }
    }
}


@Composable
fun KelivoApp() {

    var message by remember {
        mutableStateOf("")
    }


    MaterialTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            Text(
                text = "Kelivo AI",
                style = MaterialTheme.typography.headlineMedium
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                Text(
                    "聊天记录区域"
                )

            }


            Row {

                TextField(
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text("输入消息")
                    }
                )


                Spacer(
                    modifier = Modifier.width(8.dp)
                )


                Button(
                    onClick = {

                    }
                ) {

                    Text("发送")

                }

            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text("聊天")

                Text("AI")

                Text("手机")

            }

        }

    }
}
