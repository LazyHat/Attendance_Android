package ru.lazyhat.novsu.student.data.net

import io.ktor.client.HttpClient

interface NetworkSource {
}

class NetworkSourceImpl(client: HttpClient) : NetworkSource {

}