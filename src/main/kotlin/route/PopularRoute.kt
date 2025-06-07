package com.example.route

import com.example.useCase.FavoriteRepository
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

val topFavourites = favouritesList
    .groupingBy { it.sneakers_id }
    .eachCount()
    .entries
    .sortedByDescending { it.value }
    .take(15)
    .map { it.key }

fun Route.popularRoute() {
    get("/popular") {
        val favoriteRepository: FavoriteRepository = FavoriteRepository()
        var list = favoriteRepository.convertIdPopylar(topFavourites)
        call.respond(list)
    }
}
