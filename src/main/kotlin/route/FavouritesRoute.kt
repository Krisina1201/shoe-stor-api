package com.example.route

import com.example.useCase.FavoriteRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class Favourites(
    var sneakers_id: Int,
    var user_id: Int
)

var favouritesList = mutableListOf(
    Favourites(
        sneakers_id = 1,
        user_id = 1
    ),

    Favourites(
        sneakers_id = 3,
        user_id = 1
    ),

    Favourites(
        sneakers_id = 2,
        user_id = 1
    ),

    Favourites(
        sneakers_id = 8,
        user_id = 2
    ),

    Favourites(
        sneakers_id = 6,
        user_id = 2
    )
    )


fun Route.favouritesRoutes() {
    val favoriteRepository: FavoriteRepository = FavoriteRepository()
    get("/favourites/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid ID")

        var sneakersById: List<Favourites> = favoriteRepository.getFavoriteByIdUser(id)

        var favoriteInTitle = favoriteRepository.convertIdFavorite(sneakersById)

        call.respond(favoriteInTitle)
    }

    post("/users/{user_id}/favorites/shoes/{shoes_id}"){
        val user_id = call.parameters["user_id"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid user ID")
        val shoes_id = call.parameters["shoes_id"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid shoes ID")

        val answer = favoriteRepository.addFavorite(user_id, shoes_id)
        call.respond(answer)
    }

    delete("/users/{user_id}/favorites/shoes/{shoes_id}") {
        val user_id = call.parameters["user_id"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid user ID")
        val shoes_id = call.parameters["shoes_id"]?.toIntOrNull()
            ?: throw IllegalArgumentException("Invalid shoes ID")

        val answer = favoriteRepository.deleteFavorite(user_id, shoes_id)
        call.respond(answer)
    }
}