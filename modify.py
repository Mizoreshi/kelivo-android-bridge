from pathlib import Path

p = Path("android/app/src/main/java/com/kelivo/bridge/MainActivity.kt")

s = p.read_text()

if "ScreenTimeTool" not in s:
    s = s.replace(
        "import android.widget.*",
        "import android.widget.*\nimport com.kelivo.bridge.tools.ScreenTimeTool"
    )

button = """
        val screenButton = Button(this)
        screenButton.text = "读取屏幕使用时间"

        screenButton.setOnClickListener {
            result.text = ScreenTimeTool(this).getTodayUsage()
        }

"""

if "screenButton" not in s:
    s = s.replace(
        "        layout.addView(title)",
        button + "        layout.addView(title)"
    )

    s = s.replace(
        "        layout.addView(sendButton)",
        "        layout.addView(sendButton)\n        layout.addView(screenButton)"
    )

p.write_text(s)
