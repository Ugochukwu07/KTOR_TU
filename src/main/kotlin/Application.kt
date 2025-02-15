package com.example

import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.repository.UserRepository
import com.example.routing.configureRouting
import com.example.service.JwtService
import com.example.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository = userRepository)
    val  jwtService = JwtService(this, userService = userService)

    configureSerialization()
    configureSecurity(jwtService = jwtService)
    configureRouting(userService, jwtService)
}
