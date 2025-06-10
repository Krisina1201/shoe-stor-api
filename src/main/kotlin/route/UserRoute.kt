package com.example.route

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.useCase.FavoriteRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserWithItem(
    val userId: Int,
    val userName: String,
    val email: String,
    val password: String,
    val favourites: MutableList<Sneakers?>? = null,
    //val basket: MutableList<Sneakers>
)

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
    ),

    User (
        userId = 2,
        userName = "Пупка",
        email = "9876@mail.ru",
        password = "9876"
    )
)

var favoriteRepository = FavoriteRepository();

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

        val userFavourites = favoriteRepository.convertIdFavorite(favoriteRepository.getFavoriteByIdUser(user.userId))

        val userWithItem = UserWithItem(
            userId = user.userId,
            userName = user.userName,
            email = user.email,
            password = user.password,
            favourites = userFavourites
        )

        call.respond(userWithItem)
    }

    post("/registration") {
        val userRequest = call.receive<CreateUserRequest>()

        if (userList.any { it.email == userRequest.email }) {
            call.respond(HttpStatusCode.Conflict, "User with this email already exists")
            return@post
        }

        val user = User(
            userId = userList.size + 1,
            userName = userRequest.userName,
            password = userRequest.password,
            email = userRequest.email,
        )
        userList.add(user)

        //val fav = favoriteRepository.convertIdFavorite(favoriteRepository.getFavoriteByIdUser(user.userId))

        val userWithItem = UserWithItem(
            userId = user.userId,
            userName = user.userName,
            email = user.email,
            password = user.password,
            favourites = null
        )

        call.respond(userWithItem)
    }

    get("/allUsers") {
        val users = userList
        call.respond(users)
    }

    get("/infoUser/{userId}") {
        try {

            val user_id = call.parameters["userId"]?.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid user ID")

            val user = favoriteRepository.findUserByID(user_id)

            val favouritesUser = favoriteRepository.convertIdFavorite(favoriteRepository.getFavoriteByIdUser(user_id))

            val newUser = UserWithItem(
                userId = user?.userId ?: 0,
                userName = user?.userName ?: "доздраздпердра",
                email = user?.email ?: "",
                password = user?.password ?: "",
                favourites = favouritesUser

            )

            call.respond(newUser)
        } catch (e: Exception) {
            call.respond(e)
        }
    }

    authenticate("auth-jwt") {
        get("/profile/{userId}") {
            var favoriteRepository = FavoriteRepository();
            val user_id = call.parameters["userId"]?.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid user ID")

            val user = favoriteRepository.findUserByID(user_id)

            val favouritesUser = favoriteRepository.convertIdFavorite(favoriteRepository.getFavoriteByIdUser(user_id))

            val newUser = UserWithItem(
                userId = user?.userId ?: 0,
                userName = user?.userName ?: "доздраздпердра",
                email = user?.email ?: "",
                password = user?.password ?: "",
                favourites = favouritesUser

            )

            call.respond(newUser)
        }
    }
}