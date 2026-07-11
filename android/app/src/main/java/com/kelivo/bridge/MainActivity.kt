package com.kelivo.bridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kelivo.bridge.tools.ScreenTimeTool
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


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

    var page by remember {
        mutableStateOf("聊天")
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {


        Box(
            modifier = Modifier.weight(1f)
        ) {

            when(page) {

                "聊天" -> ChatPage()

                "AI" -> {
                    Text(
                        "AI设置页面",
                        modifier = Modifier.padding(20.dp)
                    )
                }


                "手机" -> {

                    val context = androidx.compose.ui.platform.LocalContext.current

                    Text(
                        ScreenTimeTool(context).getTodayUsage(),
                        modifier = Modifier.padding(20.dp)
                    )

                }

            }

        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {


            Button(
                onClick = {
                    page="聊天"
                }
            ){
                Text("聊天")
            }


            Button(
                onClick = {
                    page="AI"
                }
            ){
                Text("AI")
            }


            Button(
                onClick = {
                    page="手机"
                }
            ){
                Text("手机")
            }

        }

    }

}



@Composable
fun ChatPage(){

    var input by remember {
        mutableStateOf("")
    }


    var messages by remember {
        mutableStateOf(
            listOf<String>()
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){


        Text(
            "Kelivo AI",
            style = MaterialTheme.typography.headlineMedium
        )


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ){

            items(messages){

                Text(
                    it,
                    modifier = Modifier.padding(8.dp)
                )

            }

        }



        Row {


            TextField(
                value=input,
                onValueChange={
                    input=it
                },
                modifier=Modifier.weight(1f)
            )


            Button(
                onClick={

                    val msg=input

                    messages =
                        messages + "我: $msg"

                    input=""

                    sendMessage(
                        msg
                    ){ reply ->

                        messages =
                            messages + "AI: $reply"

                    }

                }
            ){

                Text("发送")

            }

        }

    }

}




fun sendMessage(
    text:String,
    callback:(String)->Unit
){


    val client =
        OkHttpClient()


    val json =
        """
        {
        "model":"deepseek-ai/DeepSeek-V4-Flash",
        "messages":[
        {
        "role":"user",
        "content":"$text"
        }
        ]
        }
        """.trimIndent()



    val request =
        Request.Builder()
            .url(
                "https://memory5-vuv9.onrender.com/v1/chat/completions"
            )
            .post(
                json.toRequestBody(
                    "application/json".toMediaType()
                )
            )
            .build()



    client.newCall(request)
        .enqueue(
            object:Callback{


                override fun onFailure(
                    call:Call,
                    e:IOException
                ){

                    callback(
                        "失败: ${e.message}"
                    )

                }



                override fun onResponse(
                    call:Call,
                    response:Response
                ){

                    callback(
                        response.body?.string()
                            ?: "空回复"
                    )

                }

            }
        )

}
