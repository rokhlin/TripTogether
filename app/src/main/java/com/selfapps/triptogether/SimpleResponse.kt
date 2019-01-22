package com.selfapps.triptogether

open class SimpleResponse(var response: Resp = Resp.ERROR, var message: String = "")
enum class Resp{
    SUCCESSFUL,
    ERROR
}