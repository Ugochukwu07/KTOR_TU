package com.example.routing

import com.example.model.User
import com.example.routing.request.UserRequest
import com.example.routing.response.UserResponse
import com.example.service.UserService
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoute(
    userService: UserService
) {
    post {
        val userRequest = call.receive<UserRequest>()

        val createdUser = userService.save(
            user = userRequest.toModel()
        ) ?: return@post call.respond(HttpStatusCode.BadRequest)

        call.response.header(name = "id", value = createdUser.id.toString())
        call.respond(message = HttpStatusCode.Created)
    }

    get {
        val users = userService.findAll()

        call.respond(message = users.map(User::toResponse))
    }

    get ("/{id}") {
        val id: String = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

        val user = userService.findById(id) ?: return@get call.respond (HttpStatusCode.NotFound)

        call.respond(message = user.toResponse())
    }
}

private fun UserRequest.toModel(): User = User (
    id = UUID.randomUUID(),
    username = this.username,
    password = this.password
)

private fun User.toResponse(): UserResponse = UserResponse (
    id = this.id,
    username = this.username
)
