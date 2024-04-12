package kaikt.api.entity.dsl.cardmsg

open class CountdownComponent internal constructor(val mode: String): CardComponent("countdown")

class CountdownDay(val endTime: Long): CountdownComponent("day")
class CountdownHour(val endTime: Long): CountdownComponent("hour")
class CountdownSecond(val startTime: Long, val endTime: Long): CountdownComponent("second")