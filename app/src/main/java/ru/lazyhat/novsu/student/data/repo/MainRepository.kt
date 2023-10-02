package ru.lazyhat.novsu.student.data.repo

import ru.lazyhat.novsu.student.data.net.NetworkSource

interface MainRepository {
    fun login(username: String, password: String): Boolean
    fun isLoggedIn(): Boolean
}

data class Credentials(val username: String, val password: String)

class MainRepositoryImpl(networkSource: NetworkSource) : MainRepository {
    lateinit var userToken: String
    var savedCredentials = Credentials("lazyhat", "pass")
    override fun login(username: String, password: String): Boolean {
        TODO("Not yet implemented")
    }
}