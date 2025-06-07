package com.example.route

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    val userId: Int,
    val userName: String,
    val email: String,
    val password: String,
)

@Serializable
data class CreateUserRequest(
    val userName: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthUserRequest(
    val email: String,
    val password: String
)

val userList = mutableListOf(
    User (
        userId = 1,
        userName = "Пупка",
        email = "123@mail.ru",
        password = "12345"
    )
)


private fun generateToken(user: User, environment: ApplicationEnvironment): String {
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssue = environment.config.property("jwt.issue").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtIssue)
        .withClaim("user", user.userName)
        .withClaim("userId", user.userId)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
        .sign(Algorithm.HMAC256(jwtSecret))
}

fun Route.authRoute() {
    post("/login") {
        val loginRequest = call.receive<AuthUserRequest>()

        val user = userList.firstOrNull {
            it.email == loginRequest.email && it.password == loginRequest.password
        } ?: run {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            return@post
        }

        val token = generateToken(user, environment)
        call.respond(mapOf("token" to token))
    }

    post("/registration") {
        val userRequest = call.receive<CreateUserRequest>()

        if (userList.any { it.email == userRequest.email }) {
            call.respond(HttpStatusCode.Conflict, "User with this email already exists")
            return@post
        }

        val newUser = User(
            userId = userList.size + 1,
            userName = userRequest.userName,
            password = userRequest.password,
            email = userRequest.email
        )
        userList.add(newUser)

        val token = generateToken(newUser, environment)
        call.respond(mapOf("token" to token))
    }

    get("/allUsers") {
        val users = userList
        call.respond(users)
    }

    authenticate("auth-jwt") {
        get("/profile/{userId}") {
            val userId = call.pathParameters["userId"]?.toIntOrNull()
            val user = userId?.let { id -> userList.firstOrNull { it.userId == id } }

            if (user != null) {
                call.respond(user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}