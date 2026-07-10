from http.server import BaseHTTPRequestHandler, HTTPServer
import json

class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == "/screen_time":
            data = {
                "device": "vivo",
                "today": "测试数据",
                "message": "Kelivo Android Bridge is running"
            }

            self.send_response(200)
            self.send_header("Content-type", "application/json")
            self.end_headers()

            self.wfile.write(
                json.dumps(data, ensure_ascii=False).encode()
            )

        else:
            self.send_response(404)
            self.end_headers()


server = HTTPServer(("0.0.0.0", 8080), Handler)

print("Kelivo Android Bridge running on port 8080")

server.serve_forever()
