package com.example.githubclientkmm.android.utils

//import com.zxltrxn.githubclient.data.network.RequestCode

//sealed interface Resource<out T> {
//    data class Success<T>(val data: T) : Resource<T>
//    data class Error(val message: LocalizeString, val code: RequestCode? = null) : Resource<Nothing>
//}
//
//fun <T> Resource<T>.toUnitResource(): Resource<Unit> {
//    return when (this) {
//        is Resource.Success -> Resource.Success(Unit)
//        is Resource.Error -> this
//    }
//}
//
//fun <T, R> Resource<T>.map(block: (T) -> R): Resource<R> {
//    return when (this) {
//        is Resource.Success -> {
//            val reconstructedData: R = block(this.data)
//            Resource.Success(data = reconstructedData)
//        }
//        is Resource.Error -> this
//    }
//}
