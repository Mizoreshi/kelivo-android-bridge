from pathlib import Path

p = Path("android/app/src/main/java/com/kelivo/bridge/MainActivity.kt")

s = p.read_text()

s = s.replace(
'''val client =
        OkHttpClient()''',
'''val client =
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
            .build()'''
)

p.write_text(s)
