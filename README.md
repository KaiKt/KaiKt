# KaiKt
开黑啦 API 的 Kotlin 实现

## 使用（框架）

### 仅 HttpApi

```kotlin

val api = KToken("BotToken").toApi() // 获取 Api 实例

api.Guild().getGuilList() // 使用 Api

```

### WebSocket 客户端

```kotlin
val api = KToken("BotToken").toApi()
val cli = KaiClient(api) // 获取 WS 客户端

fun start() {
	cli.eventBus.register(this) // 将当前类注册为事件监听器
    cli.connect()
}

@org.greenrobot.eventbus.Subscribe
fun onGuildTextMessage(e: GuildTextMessageEvent) {
	// 处理事件
}

```

## 使用（辅助）

### CardMessage 构造器

```kotlin
val cardMessageStr = buildCardMessage {
	header("这里是标题")
    textAndImage("这个是带有配图的文字", "imgUrl")
    // ...
}
```