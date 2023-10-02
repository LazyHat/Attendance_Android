package ru.lazyhat.novsu.student.data.net

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

private val authority = "http://192.168.0.103:8080"
//private val authority = "http://lazyhat.ru"

interface NetworkSource {
    suspend fun validateToken(token: String): Boolean
    suspend fun logIn(username: String, password: String): String?
}

class NetworkSourceImpl(private val client: HttpClient) : NetworkSource {
    override suspend fun validateToken(token: String): Boolean =
        client.get("$authority/user/token-info").let {
            when (it.status) {
                HttpStatusCode.OK -> true
                else -> false
            }
        }

    override suspend fun logIn(username: String, password: String): String? =
        client.get("$authority/student/login?username=$username&password=$password")
            .takeIf { it.status == HttpStatusCode.OK }?.bodyAsText()
}