package com.zxltrxn.githubclient.data.network

enum class RequestCode(val code: Int) {
    NO_INTERNET_CODE(0),
    WRONG_TOKEN_CODE(401),
    NO_RIGHTS_CODE(403),
    NOT_FOUND_CODE(404)
}