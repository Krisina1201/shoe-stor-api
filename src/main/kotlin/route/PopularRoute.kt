package com.example.route

import com.example.useCase.FavoriteRepository
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable



fun Route.popularRoute() {
    get("/popular") {
        val topFavourites = favouritesList
            .groupingBy { it.sneakers_id }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(15)
            .map { it.key }

        val favoriteRepository: FavoriteRepository = FavoriteRepository()
        var list = favoriteRepository.convertIdPopylar(topFavourites)
        call.respond(list)
    }
}
