package com.kelivo.bridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kelivo.bridge.tools.ScreenTimeTool
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


data class Message(
    val text: String,
    val user: Boolean
)


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            KelivoApp()

        }

    }
}



@Composable
fun KelivoApp(){

    var page by remember {
        mutableStateOf("聊天")
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ){

        Box(
            modifier = Modifier.weight(1f)
        ){

            when(page){

                "聊天" -> ChatPage()

                "AI" -> Text(
                    "AI助手管理\n\n以后这里创建AI",
                    modifier = Modifier.padding(20.dp)
                )


                "动态" -> Text(
                    "动态空间\n\n以后这里做AI朋友圈",
                    modifier = Modifier.padding(20.dp)
                )


                "我的" -> {

                    val context =
                        androidx.compose.ui.platform.LocalContext.current

                    Text(
                        "手机状态\n\n"+
                        ScreenTimeTool(context).getTodayUsage(),
                        modifier = Modifier.padding(20.dp)
                    )

                }

            }

        }


        NavigationBar{

            NavigationBarItem(
                selected = page=="聊天",
                onClick = {
                    page="聊天"
                },
                label = {
                    Text("聊天")
                },
                icon = {}
            )


            NavigationBarItem(
                selected = page=="AI",
                onClick = {
                    page="AI"
                },
                label = {
                    Text("AI")
                },
                icon = {}
            )


            NavigationBarItem(
                selected = page=="动态",
                onClick = {
                    page="动态"
                },
                label = {
                    Text("动态")
                },
                icon = {}
            )


            NavigationBarItem(
                selected = page=="我的",
                onClick = {
                    page="我的"
                },
                label = {
                    Text("我的")
                },
                icon = {}
            )

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
            listOf<Message>()
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                "◉ 砚",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Text(
                "在线",
                style = MaterialTheme.typography.bodyMedium
            )

        }


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ){

            items(messages){

                MessageBubble(it)

            }

        }



        Row(
            verticalAlignment = Alignment.CenterVertically
        ){

            TextField(
                value = input,
                onValueChange = {
                    input = it
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

                    if(input.isBlank()){
                        return@Button
                    }


                    val send =
                        input


                    messages =
                        messages +
                        Message(
                            send,
                            true
                        )


                    input = ""


                    sendMessage(
                        send
                    ){ reply ->


                        messages =
                            messages +
                            Message(
                                reply,
                                false
                            )

                    }


                }
            ){

                Text("发送")

            }


        }

    }

}




@Composable
fun MessageBubble(
    message: Message
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement =
            if(message.user)
                Arrangement.End
            else
                Arrangement.Start
    ){

        Surface(
            shape =
                RoundedCornerShape(16.dp),
            tonalElevation = 2.dp
        ){

            Text(
                text = message.text,
                modifier =
                    Modifier.padding(12.dp)
            )

        }

    }

}





fun sendMessage(
    text:String,
    callback:(String)->Unit
){

    val client =
        OkHttpClient.Builder()
            .connectTimeout(
                120,
                java.util.concurrent.TimeUnit.SECONDS
            )
            .readTimeout(
                120,
                java.util.concurrent.TimeUnit.SECONDS
            )
            .writeTimeout(
                120,
                java.util.concurrent.TimeUnit.SECONDS
            )
            .build()


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
            object: Callback{


                override fun onFailure(
                    call: Call,
                    e: IOException
                ){

                    callback(
                        "连接失败: ${e.message}"
                    )

                }



                override fun onResponse(
                    call: Call,
                    response: Response
                ){

                    try {

                        val body =
                            response.body?.string()
                                ?: ""


                        val result =
                            JSONObject(body)
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                                .replace("。", "。\\n\\n")
                                .replace("！", "！\\n\\n")
                                .replace("？", "？\\n\\n")


                        callback(result)


                    }catch(e:Exception){

                        callback(
                            "解析失败"
                        )

                    }

                }


            }
        )

}
