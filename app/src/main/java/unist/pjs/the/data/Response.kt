package unist.pjs.the.data

import com.google.gson.annotations.SerializedName


data class Confirm(
    @SerializedName("ok")
    var ok: String = "",
    @SerializedName("error")
    var error: String = ""
)
