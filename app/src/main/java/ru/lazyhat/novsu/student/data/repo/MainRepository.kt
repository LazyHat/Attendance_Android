package ru.lazyhat.novsu.student.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.lazyhat.novsu.student.data.net.NetworkSource

interface MainRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun checkLoggedInOrLogIn(): Boolean
}

data class Credentials(val username: String, val password: String)

class MainRepositoryImpl(private val networkSource: NetworkSource) : MainRepository {
    lateinit var userToken: String
    var savedCredentials: Credentials? = Credentials("lazyhat", "pass")
    override suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        networkSource.logIn(username, password)?.let {
            userToken = it
            true
        } ?: false
    }

    override suspend fun checkLoggedInOrLogIn(): Boolean = withContext(Dispatchers.IO) {
        if (this@MainRepositoryImpl::userToken.isInitialized) {
            networkSource.validateToken(userToken)
        } else {
            savedCredentials?.let {
                login(it.username, it.password)
            } ?: false
        }
    }
}