# KaiKt
开黑啦 API 的 Kotlin 实现

## 使用

```kotlin

val api = KToken("BotToken").toApi() // 获取 Api 实例

api.Guild().getGuilList() // 使用 Api

```

**近期将对 HttpApi（KaiApi.kt） 部分进行整体调整，请以 dev/websocket 分支为准。**
