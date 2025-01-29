# How to use site title downloader

#### Request:
```bash
curl --location 'http://localhost:8080/title' \
--header 'Content-Type: application/json' \
--data '["https://zio.dev","https://github.com"]'
```

#### Response:
```json
{"https://zio.dev":"ZIO | ZIO","https://github.com":"GitHub · Build and ship software on a single, collaborative platform · GitHub"}
```