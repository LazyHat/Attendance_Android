package ru.lazyhat.novsu.student.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.lazyhat.novsu.student.data.net.NetworkSource
import ru.lazyhat.novsu.student.data.net.NetworkSourceImpl
import ru.lazyhat.novsu.student.data.repo.MainRepository
import ru.lazyhat.novsu.student.data.repo.MainRepositoryImpl
import ru.lazyhat.novsu.student.ui.screens.login.LoginScreenViewModel

val module = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
    single<NetworkSource> { NetworkSourceImpl(get()) }
    single<MainRepository> { MainRepositoryImpl(get()) }

    viewModelOf(::LoginScreenViewModel)
}