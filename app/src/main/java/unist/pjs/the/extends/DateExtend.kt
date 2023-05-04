package unist.pjs.the.extends

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

enum class TimeValue(val value: Int,val maximum : Int, val msg : String) {
    SEC(60,60,"분 전"),
    MIN(60,24,"시간 전"),
    HOUR(24,30,"일 전"),
    DAY(30,12,"달 전"),
    MONTH(12,Int.MAX_VALUE,"년 전")
}

fun Long.timeDiff(): String{

    val curTime = System.currentTimeMillis() - 32400000
    var diffTime = (curTime- this) / 1000
    var msg: String? = null
    if(diffTime < TimeValue.SEC.value )
        msg= "초 전"
    else {
        for (i in TimeValue.values()) {
            diffTime /= i.value
            if (diffTime < i.maximum) {
                msg = i.msg
                break
            }
        }
    }
    return "$diffTime$msg"
}

fun Long.timeDiff2(): String{

    val curTime = System.currentTimeMillis()
    var diffTime = (curTime- this) / 1000
    var msg: String? = null
    if(diffTime < TimeValue.SEC.value )
        msg= "초 전"
    else {
        for (i in TimeValue.values()) {
            diffTime /= i.value
            if (diffTime < i.maximum) {
                msg = i.msg
                break
            }
        }
    }
    return "$diffTime$msg"
}

fun String.convertDateToTimestamp(): Long {
    val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    val sdf = SimpleDateFormat(format)

    return sdf.parse(this).time
}

fun Long.convertTimestampToDate(): String {
    val sdf = SimpleDateFormat("a h:mm")
    return sdf.format(this)
}

fun Long.convertTimestampToDate2(): String {
    val sdf = SimpleDateFormat("MM/dd hh:mm")
    return sdf.format(this + 32400000)
}