package ru.lazyhat.novsu.student.data.net

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

private val authority = "http://192.168.0.103:8080"
//private val authority = "http://lazyhat.ru"

interface NetworkSource {
    suspend fun validateUserToken(token: String): Boolean
    suspend fun logIn(username: String, password: String): String?
    suspend fun registerToLesson(token: String, userToken: String): Boolean
}

class NetworkSourceImpl(private val client: HttpClient) : NetworkSource {
    override suspend fun validateUserToken(token: String): Boolean = false
//        client.get("$authority/user/token-info").let {
//            when (it.status) {
//                HttpStatusCode.OK -> true
//                else -> false
//            }
//        }

    override suspend fun logIn(username: String, password: String): String? =
        client.get("$authority/student/login?username=$username&password=$password")
            .takeIf { it.status == HttpStatusCode.OK }?.bodyAsText()

    override suspend fun registerToLesson(token: String, userToken: String): Boolean =
        client.get("$authority/student/register?token=$token") {
            headers["Authorization"] = "Bearer $userToken"
        }.status == HttpStatusCode.OK

//    private suspend inline fun <T> createRequest(
//        url: String,
//        block: HttpRequestBuilder.() -> Unit
//    ): ResponseBody<T> = try {
//        client.request(url, block).let {
//            when (it.status) {
//                HttpStatusCode.OK -> ResponseBody.Success(it.body())
//                else -> Re
//            }
//        }
//    } catch (e: IOException) {
//        ResponseBody.Error.IOError("R")
//    }
}