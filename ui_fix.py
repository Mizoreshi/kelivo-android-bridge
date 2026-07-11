from pathlib import Path

p = Path("android/app/src/main/java/com/kelivo/bridge/MainActivity.kt")

s = p.read_text()

s = s.replace(
'''Text(
            "Kelivo AI",
            style = MaterialTheme.typography.headlineMedium
        )''',
'''Row(
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

        }'''
)


s = s.replace(
'''val result =
                            JSONObject(body)
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")''',
'''val result =
                            JSONObject(body)
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                                .replace("。", "。\\\\n\\\\n")
                                .replace("！", "！\\\\n\\\\n")
                                .replace("？", "？\\\\n\\\\n")'''
)


p.write_text(s)
